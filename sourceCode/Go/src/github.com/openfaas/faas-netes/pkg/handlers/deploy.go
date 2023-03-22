// Copyright (c) Alex Ellis 2017. All rights reserved.
// Copyright 2020 OpenFaaS Author(s)
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package handlers

import (
	"context"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"math/rand"
	"net/http"
	"sort"
	"strconv"
	"strings"
	"time"

	scheduler "github.com/openfaas/faas-netes/gpu/controller"
	"github.com/openfaas/faas-netes/gpu/repository"
	gTypes "github.com/openfaas/faas-netes/gpu/types"
	"github.com/openfaas/faas-netes/pkg/k8s"
	types "github.com/openfaas/faas-provider/types"
	corev1 "k8s.io/api/core/v1"
	"k8s.io/apimachinery/pkg/api/resource"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/util/intstr"
	"k8s.io/client-go/kubernetes"
)

// initialReplicasCount how many replicas to start of creating for a function
const initialReplicasCount = 1
const initialMaxReplicasCount = 20
const initialScaleToZero = "false"
const initialMaxBatchSize = 1
const initialInactiveNum = 3
const MonitorInterval = time.Second * 10

// MakeDeployHandler creates a handler to create new functions in the cluster
func MakeDeployHandler(functionNamespace string, factory k8s.FunctionFactory, clientset *kubernetes.Clientset) http.HandlerFunc {
	secrets := k8s.NewSecretsClient(factory.Client)

	return func(w http.ResponseWriter, r *http.Request) {
		// ctx := r.Context()

		if r.Body != nil {
			defer r.Body.Close()
		}

		body, _ := io.ReadAll(r.Body)

		request := types.FunctionDeployment{}
		err := json.Unmarshal(body, &request)
		if err != nil {
			wrappedErr := fmt.Errorf("deploy: failed to unmarshal request: %s", err.Error())
			http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
			return
		}

		if err := ValidateDeployRequest(&request); err != nil {
			wrappedErr := fmt.Errorf("deploy: validation failed: %s", err.Error())
			http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
			return
		}

		namespace := functionNamespace
		if len(request.Namespace) > 0 {
			namespace = request.Namespace
		}

		if namespace != functionNamespace {
			http.Error(w, fmt.Sprintf("valid namespaces are: %s", functionNamespace), http.StatusBadRequest)
			return
		}

		existingSecrets, err := secrets.GetSecrets(namespace, request.Secrets)
		if err != nil {
			wrappedErr := fmt.Errorf("unable to fetch secrets: %s", err.Error())
			http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
			return
		}

		repository.RegisterFuncDeploy(request.Service)
		repository.UpdateFuncProfileCache(&gTypes.FuncProfile{
			FunctionName:    request.Service,
			MaxCpuCoreUsage: 0.9,
			MinCpuCoreUsage: 0.1,
			AvgCpuCoreUsage: rand.Float64(),
		})

		// get the init replicas of function default=1
		initialReplicas := int32p(initialReplicasCount)
		if request.Labels != nil {
			// min
			/*if min := getMinReplicaCount(*request.Labels); min != nil {
				initialReplicas = min
				repository.UpdateFuncMinReplicas(request.Service, *min)
			} else {
				repository.UpdateFuncMinReplicas(request.Service, *initialReplicas)
			}
			// max
			if max := getMaxReplicaCount(*request.Labels); max != nil {
				repository.UpdateFuncMaxReplicas(request.Service, *max)
			} else {
				repository.UpdateFuncMaxReplicas(request.Service, *int32p(initialMaxReplicasCount))
			}*/
			// scale to zero
			if scaleToZero := getScaleToZero(*request.Labels); scaleToZero != nil {
				repository.UpdateFuncScaleToZero(request.Service, *scaleToZero)
			} else {
				repository.UpdateFuncScaleToZero(request.Service, initialScaleToZero)
			}
			// CPU core bind
			//if cpuCoreBind := getCpuCoreBind(*request.Labels); cpuCoreBind != nil {
			//	repository.UpdateFuncCpuCoreBind(request.Service, *cpuCoreBind)
			//} else {
			//	repository.UpdateFuncCpuCoreBind(request.Service, initialCpuCoreBind)
			//}
			// supported batch size
			if inactiveNum := getInactiveNum(*request.Labels); inactiveNum != nil {
				repository.UpdateFuncInactiveNum(request.Service, *inactiveNum)
			} else {
				repository.UpdateFuncInactiveNum(request.Service, initialInactiveNum)
			}
			// supported batch size
			if maxBatchSize := getMaxBatchSize(*request.Labels); maxBatchSize != nil {
				repository.UpdateFuncSupportBatchSize(request.Service, *maxBatchSize)
			} else {
				repository.UpdateFuncSupportBatchSize(request.Service, initialMaxBatchSize)
			}

		}
		qpsPerInstances, err := strconv.ParseFloat(request.QpsPerInstance, 64)
		if err != nil {
			log.Println(err.Error())
			return
		} else {
			repository.UpdateFuncQpsPerInstance(request.Service, qpsPerInstances)
		}

		repository.UpdateFuncRequestResources(request.Service, request.Requests)
		repository.UpdateFuncConstrains(request.Service, request.Constraints)
		repository.UpdateFuncExpectedReplicas(request.Service, 0)
		repository.UpdateFuncAvailReplicas(request.Service, 0)

		//var latestPodSpec *corev1.Pod
		for i := *int32p(0); i < *initialReplicas; i++ {
			podSpec, specErr := makePodSpec(request, existingSecrets, factory)
			if specErr != nil {
				wrappedErr := fmt.Errorf("deploy: failed make Pod spec for replica = %d: %s \n", i+1, specErr.Error())
				log.Println(wrappedErr.Error())
				http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
				return
			}
			//log.Printf("deploy: Deployment (pods with replicas = %d) created: funcName= %s, namespace= %s \n", i+1, request.Service, namespace)

			// after that, deploy the service to find the pods with special label
			serviceSpec, err := makeServiceSpec(request, factory)
			if err != nil {
				wrappedErr := fmt.Errorf("failed create Service spec: %s", err.Error())
				log.Println(wrappedErr)
				http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
				return
			}

			_, err = factory.Client.CoreV1().Services(namespace).Create(context.TODO(), serviceSpec, metav1.CreateOptions{})
			if err != nil {
				wrappedErr := fmt.Errorf("deploy: failed create Service: %s \n", err.Error())
				log.Println(wrappedErr)
				http.Error(w, wrappedErr.Error(), http.StatusInternalServerError)
				return
			}
			//log.Printf("deploy: service created: %s.%s \n", request.Service, namespace)
			repository.UpdateFuncSpec(request.Service, podSpec, serviceSpec)
			repository.AddVirtualFuncPodConfig(request.Service) // add one virtual pod

		}
		scheduler.InitProfiler()
		go scheduler.CreatePreWarmPod(request.Service, namespace, qpsPerInstances, 1, clientset)
		go instanceScaleMonitor(request.Service, functionNamespace, clientset) // Launch a go routine to scaling function instances

		w.WriteHeader(http.StatusAccepted)
		return

		/*deploymentSpec, specErr := makeDeploymentSpec(request, existingSecrets, factory)

		var profileList []k8s.Profile
		if request.Annotations != nil {
			profileNamespace := factory.Config.ProfilesNamespace
			profileList, err = factory.GetProfiles(ctx, profileNamespace, *request.Annotations)
			if err != nil {
				wrappedErr := fmt.Errorf("failed create Deployment spec: %s", err.Error())
				log.Println(wrappedErr)
				http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
				return
			}
		}
		for _, profile := range profileList {
			factory.ApplyProfile(profile, deploymentSpec)
		}

		if specErr != nil {
			wrappedErr := fmt.Errorf("failed create Deployment spec: %s", specErr.Error())
			log.Println(wrappedErr)
			http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
			return
		}

		deploy := factory.Client.AppsV1().Deployments(namespace)

		_, err = deploy.Create(context.TODO(), deploymentSpec, metav1.CreateOptions{})
		if err != nil {
			wrappedErr := fmt.Errorf("unable create Deployment: %s", err.Error())
			log.Println(wrappedErr)
			http.Error(w, wrappedErr.Error(), http.StatusInternalServerError)
			return
		}

		log.Printf("Deployment created: %s.%s\n", request.Service, namespace)

		service := factory.Client.CoreV1().Services(namespace)
		serviceSpec, err := makeServiceSpec(request, factory)
		if err != nil {
			wrappedErr := fmt.Errorf("failed create Service spec: %s", err.Error())
			log.Println(wrappedErr)
			http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
			return
		}

		if _, err = service.Create(context.TODO(), serviceSpec, metav1.CreateOptions{}); err != nil {
			wrappedErr := fmt.Errorf("failed create Service: %s", err.Error())
			log.Println(wrappedErr)
			http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
			return
		}

		log.Printf("Service created: %s.%s\n", request.Service, namespace)

		w.WriteHeader(http.StatusAccepted*/
	}
}

func makePodSpec(request types.FunctionDeployment, existingSecrets map[string]*corev1.Secret, factory k8s.FunctionFactory) (*corev1.Pod, error) {
	envVars := buildEnvVars(&request) // prase self-defined environments in faas-cli deploy yaml

	labels := map[string]string{
		"faas_function": request.Service,
	}

	if request.Labels != nil {
		for k, v := range *request.Labels {
			labels[k] = v
		}
	}

	// GPU card selection start
	nodeSelector := map[string]string{} // init=map{}

	// build the node selector
	//nodeLabelStrList := strings.Split(repository.GetClusterCapConfig().
	//	ClusterCapacity[0].NodeLabel, "=")
	//nodeSelector[nodeLabelStrList[0]] = nodeLabelStrList[1]

	envVars = append(envVars, corev1.EnvVar{ // env parameter
		Name:  "CUDA_VISIBLE_DEVICES",
		Value: "-1",
	})
	envVars = append(envVars, corev1.EnvVar{ // env parameter
		Name:  "CUDA_MPS_ACTIVE_THREAD_PERCENTAGE",
		Value: "0",
	})

	envVars = append(envVars, corev1.EnvVar{ // tfserving exec command parameter
		Name:  "GPU_MEM_FRACTION",
		Value: "0",
	})

	envVars = append(envVars, corev1.EnvVar{ // tfserving exec command parameter
		Name:  "BATCH_SIZE",
		Value: "0",
	})

	envVars = append(envVars, corev1.EnvVar{ // tfserving exec command parameter
		Name:  "BATCH_TIMEOUT",
		Value: "0",
	})

	//log.Println("deploy: GPU resource envVars = ", envVars)

	resources, resourceErr := createResources(request) // only for CPU and host memory
	if resourceErr != nil {
		return nil, resourceErr
	}

	var imagePullPolicy corev1.PullPolicy
	switch factory.Config.ImagePullPolicy {
	case "Never":
		imagePullPolicy = corev1.PullNever
	case "IfNotPresent":
		imagePullPolicy = corev1.PullIfNotPresent
	default:
		imagePullPolicy = corev1.PullAlways
	}

	annotations, err := buildAnnotations(request)
	if err != nil {
		return nil, err
	}
	var serviceAccount string

	if request.Annotations != nil {
		annotations = *request.Annotations
		if val, ok := annotations["com.openfaas.serviceaccount"]; ok && len(val) > 0 {
			serviceAccount = val
		}
	}

	probes, err := factory.MakeProbes(request)
	if err != nil {
		return nil, err
	}

	pod := &corev1.Pod{
		TypeMeta: metav1.TypeMeta{
			Kind:       "Pod",
			APIVersion: "v1",
		},
		ObjectMeta: metav1.ObjectMeta{
			//sleep2-pod.1.-1.951225,
			Name:        request.Service + "-pod-n0g-1-00000000",
			Annotations: annotations, //prometheus.io.scrape: false
			Labels:      labels,
			//{labels: com.openfaas.scale.max=15 com.openfaas.scale.min=1 com.openfaas.scale.zero=true
			//faas_function=mnist-test uid=44642818}
		},
		Spec: corev1.PodSpec{
			//HostIPC: true,
			NodeSelector: nodeSelector,
			Containers: []corev1.Container{
				{
					Name:  request.Service + "-con",
					Image: request.Image,
					Ports: []corev1.ContainerPort{
						{
							ContainerPort: factory.Config.RuntimeHTTPPort,
							Protocol:      corev1.ProtocolTCP},
					},
					Env:             envVars,
					Resources:       *resources,
					ImagePullPolicy: imagePullPolicy,
					LivenessProbe:   probes.Liveness,
					ReadinessProbe:  probes.Readiness,
					SecurityContext: &corev1.SecurityContext{
						ReadOnlyRootFilesystem: &request.ReadOnlyRootFilesystem,
					},
				},
			},
			ServiceAccountName: serviceAccount,
			RestartPolicy:      corev1.RestartPolicyAlways,
			DNSPolicy:          corev1.DNSClusterFirst,
		},
	}

	factory.ConfigureReadOnlyRootFilesystem(request, pod)
	factory.ConfigureContainerUserID(pod)

	if err = factory.ConfigureSecrets(request, pod, existingSecrets); err != nil {
		return nil, err
	}

	return pod, nil
}

/*func makeDeploymentSpec(request types.FunctionDeployment, existingSecrets map[string]*apiv1.Secret, factory k8s.FunctionFactory) (*appsv1.Deployment, error) {
	envVars := buildEnvVars(&request)

	initialReplicas := int32p(initialReplicasCount)
	labels := map[string]string{
		"faas_function": request.Service,
	}

	if request.Labels != nil {
		if min := getMinReplicaCount(*request.Labels); min != nil {
			initialReplicas = min
		}
		for k, v := range *request.Labels {
			labels[k] = v
		}
	}

	nodeSelector := createSelector(request.Constraints)

	resources, err := createResources(request)

	if err != nil {
		return nil, err
	}

	var imagePullPolicy apiv1.PullPolicy
	switch factory.Config.ImagePullPolicy {
	case "Never":
		imagePullPolicy = apiv1.PullNever
	case "IfNotPresent":
		imagePullPolicy = apiv1.PullIfNotPresent
	default:
		imagePullPolicy = apiv1.PullAlways
	}

	annotations, err := buildAnnotations(request)
	if err != nil {
		return nil, err
	}

	probes, err := factory.MakeProbes(request)
	if err != nil {
		return nil, err
	}

	deploymentSpec := &appsv1.Deployment{
		ObjectMeta: metav1.ObjectMeta{
			Name:        request.Service,
			Annotations: annotations,
			Labels: map[string]string{
				"faas_function": request.Service,
			},
		},
		Spec: appsv1.DeploymentSpec{
			Selector: &metav1.LabelSelector{
				MatchLabels: map[string]string{
					"faas_function": request.Service,
				},
			},
			Replicas: initialReplicas,
			Strategy: appsv1.DeploymentStrategy{
				Type: appsv1.RollingUpdateDeploymentStrategyType,
				RollingUpdate: &appsv1.RollingUpdateDeployment{
					MaxUnavailable: &intstr.IntOrString{
						Type:   intstr.Int,
						IntVal: int32(0),
					},
					MaxSurge: &intstr.IntOrString{
						Type:   intstr.Int,
						IntVal: int32(1),
					},
				},
			},
			RevisionHistoryLimit: int32p(10),
			Template: apiv1.PodTemplateSpec{
				ObjectMeta: metav1.ObjectMeta{
					Name:        request.Service,
					Labels:      labels,
					Annotations: annotations,
				},
				Spec: apiv1.PodSpec{
					NodeSelector: nodeSelector,
					Containers: []apiv1.Container{
						{
							Name:  request.Service,
							Image: request.Image,
							Ports: []apiv1.ContainerPort{
								{
									Name:          "http",
									ContainerPort: factory.Config.RuntimeHTTPPort,
									Protocol:      corev1.ProtocolTCP,
								},
							},
							Env:             envVars,
							Resources:       *resources,
							ImagePullPolicy: imagePullPolicy,
							LivenessProbe:   probes.Liveness,
							ReadinessProbe:  probes.Readiness,
							SecurityContext: &corev1.SecurityContext{
								ReadOnlyRootFilesystem: &request.ReadOnlyRootFilesystem,
							},
						},
					},
					RestartPolicy: corev1.RestartPolicyAlways,
					DNSPolicy:     corev1.DNSClusterFirst,
				},
			},
		},
	}

	factory.ConfigureReadOnlyRootFilesystem(request, deploymentSpec)
	factory.ConfigureContainerUserID(deploymentSpec)

	if err := factory.ConfigureSecrets(request, deploymentSpec, existingSecrets); err != nil {
		return nil, err
	}

	return deploymentSpec, nil
}*/

func makeServiceSpec(request types.FunctionDeployment, factory k8s.FunctionFactory) (*corev1.Service, error) {
	annotations, err := buildAnnotations(request)
	if err != nil {
		return nil, err
	}

	serviceSpec := &corev1.Service{
		TypeMeta: metav1.TypeMeta{
			Kind:       "Service",
			APIVersion: "v1",
		},
		ObjectMeta: metav1.ObjectMeta{
			Name:        request.Service,
			Annotations: annotations,
		},
		Spec: corev1.ServiceSpec{
			Type: corev1.ServiceTypeClusterIP,
			Selector: map[string]string{
				"faas_function": request.Service,
			},
			Ports: []corev1.ServicePort{
				{
					Name:     "http",
					Protocol: corev1.ProtocolTCP,
					Port:     factory.Config.RuntimeHTTPPort,
					TargetPort: intstr.IntOrString{
						Type:   intstr.Int,
						IntVal: factory.Config.RuntimeHTTPPort,
					},
				},
			},
		},
	}

	return serviceSpec, nil
}

func buildAnnotations(request types.FunctionDeployment) (map[string]string, error) {
	var annotations map[string]string
	if request.Annotations != nil {
		annotations = *request.Annotations
	} else {
		annotations = map[string]string{}
	}

	if v, ok := annotations["topic"]; ok {
		if strings.Contains(v, ",") {
			return nil, fmt.Errorf("the topic annotation may only support one value in the Community Edition")
		}
	}

	for k, _ := range annotations {
		if strings.Contains(k, "amazonaws.com") || strings.Contains(k, "gke.io") {
			return nil, fmt.Errorf("annotation %q is not supported in the Community Edition", k)
		}
	}

	if _, ok := annotations["prometheus.io.scrape"]; !ok {
		annotations["prometheus.io.scrape"] = "false"
	}
	return annotations, nil
}

func buildEnvVars(request *types.FunctionDeployment) []corev1.EnvVar {
	envVars := []corev1.EnvVar{}

	if len(request.EnvProcess) > 0 {
		envVars = append(envVars, corev1.EnvVar{
			Name:  k8s.EnvProcessName,
			Value: request.EnvProcess,
		})
	}

	for k, v := range request.EnvVars {
		envVars = append(envVars, corev1.EnvVar{
			Name:  k,
			Value: v,
		})
	}

	sort.SliceStable(envVars, func(i, j int) bool {
		return strings.Compare(envVars[i].Name, envVars[j].Name) == -1
	})

	return envVars
}

func int32p(i int32) *int32 {
	return &i
}

func int64p(i int64) *int64 {
	return &i
}

func createSelector(constraints []string) map[string]string {
	selector := make(map[string]string)

	if len(constraints) > 0 {
		for _, constraint := range constraints {
			parts := strings.Split(constraint, "=")

			if len(parts) == 2 {
				selector[parts[0]] = parts[1]
			}
		}
	}

	return selector
}

func createResources(request types.FunctionDeployment) (*corev1.ResourceRequirements, error) {
	resources := &corev1.ResourceRequirements{
		Limits:   corev1.ResourceList{},
		Requests: corev1.ResourceList{},
	}

	// Set Memory limits
	if request.Limits != nil && len(request.Limits.Memory) > 0 {
		qty, err := resource.ParseQuantity(request.Limits.Memory)
		if err != nil {
			return resources, err
		}
		resources.Limits[corev1.ResourceMemory] = qty
	}

	if request.Requests != nil && len(request.Requests.Memory) > 0 {
		qty, err := resource.ParseQuantity(request.Requests.Memory)
		if err != nil {
			return resources, err
		}
		resources.Requests[corev1.ResourceMemory] = qty
	}

	// Set CPU limits
	if request.Limits != nil && len(request.Limits.CPU) > 0 {
		qty, err := resource.ParseQuantity(request.Limits.CPU)
		if err != nil {
			return resources, err
		}
		resources.Limits[corev1.ResourceCPU] = qty
	}

	if request.Requests != nil && len(request.Requests.CPU) > 0 {
		qty, err := resource.ParseQuantity(request.Requests.CPU)
		if err != nil {
			return resources, err
		}
		resources.Requests[corev1.ResourceCPU] = qty
	}

	return resources, nil
}

/*func createResources(request types.FunctionDeployment) (*apiv1.ResourceRequirements, error) {
	resources := &apiv1.ResourceRequirements{
		Limits:   apiv1.ResourceList{},
		Requests: apiv1.ResourceList{},
	}

	// Set Memory limits
	if request.Limits != nil && len(request.Limits.Memory) > 0 {
		qty, err := resource.ParseQuantity(request.Limits.Memory)
		if err != nil {
			return resources, err
		}
		resources.Limits[apiv1.ResourceMemory] = qty
	}

	if request.Requests != nil && len(request.Requests.Memory) > 0 {
		qty, err := resource.ParseQuantity(request.Requests.Memory)
		if err != nil {
			return resources, err
		}
		resources.Requests[apiv1.ResourceMemory] = qty
	}

	// Set CPU limits
	if request.Limits != nil && len(request.Limits.CPU) > 0 {
		qty, err := resource.ParseQuantity(request.Limits.CPU)
		if err != nil {
			return resources, err
		}
		resources.Limits[apiv1.ResourceCPU] = qty
	}

	if request.Requests != nil && len(request.Requests.CPU) > 0 {
		qty, err := resource.ParseQuantity(request.Requests.CPU)
		if err != nil {
			return resources, err
		}
		resources.Requests[apiv1.ResourceCPU] = qty
	}

	return resources, nil
}*/

func getMinReplicaCount(labels map[string]string) *int32 {
	if value, exists := labels["com.openfaas.scale.min"]; exists {
		minReplicas, err := strconv.Atoi(value)
		if err == nil && minReplicas > 0 {
			return int32p(int32(minReplicas))
		}

		log.Println(err)
	}

	return nil
}

func getMaxReplicaCount(labels map[string]string) *int32 {
	if value, exists := labels["com.openfaas.scale.max"]; exists {
		maxReplicas, err := strconv.Atoi(value)
		if err == nil {
			return int32p(int32(maxReplicas))
		}
		log.Println(err)
	}
	return nil
}
func getScaleToZero(labels map[string]string) *string {
	if value, exists := labels["com.openfaas.scale.zero"]; exists {
		scaleToZero := value
		return &scaleToZero
	}
	return nil
}

/*func getCpuCoreBind(labels map[string]string) *string {
	if value, exists := labels["com.openfaas.cpu.bind"]; exists {
		cpuCoreBindStr := strings.ReplaceAll(value,".",",")
		return &cpuCoreBindStr
	}
	return nil
}*/
func getInactiveNum(labels map[string]string) *int32 {
	if value, exists := labels["com.openfaas.inactive.num"]; exists {
		inactiveNum, err := strconv.Atoi(value)
		if err == nil && inactiveNum > 0 {
			return int32p(int32(inactiveNum))
		}
	}
	return nil
}
func getMaxBatchSize(labels map[string]string) *int32 {
	if value, exists := labels["com.openfaas.max.batch"]; exists {
		maxBatchSize, err := strconv.Atoi(value)
		if err == nil && maxBatchSize > 0 {
			return int32p(int32(maxBatchSize))
		}
	}
	return nil
}
func instanceScaleMonitor(funcName string, namespace string, clientset *kubernetes.Clientset) {
	log.Printf("deploy: function scaling go rountine starts namespace=%s, functioName=%s, monitor interval=%ds ......", namespace, funcName, MonitorInterval/1000000000)
	ticker := time.NewTicker(MonitorInterval)
	quit := make(chan struct{})
	writeLock := repository.GetFuncScalingLockState(funcName)
	for {
		select {
		case <-ticker.C:
			funcDeployStatus := repository.GetFunc(funcName)
			if funcDeployStatus == nil {
				log.Printf("deploy: function %s is not in repository, exist scaling in go routine \n", funcName)
				return
			}

			funcConfig, exist := funcDeployStatus.FuncPodConfigMap["v"]
			if exist {
				//if float32(funcConfig.ReqPerSecondLottery) > 0 {

				if funcDeployStatus.FuncRealRps > 0 && float32(funcConfig.ReqPerSecondLottery)/float32(funcDeployStatus.FuncRealRps) > 0.03 {
					//log.Printf("deploy: scale up function pods with vpod lottery= %d, FuncRealRps=%d for function %s ......\n",
					//	funcConfig.ReqPerSecondLottery, funcDeployStatus.FuncRealRps, funcName)
					writeLock.Lock()
					scheduler.ScaleUpFuncCapacity(funcName, namespace, funcDeployStatus.FuncQpsPerInstance, funcConfig.ReqPerSecondLottery, funcDeployStatus.FuncSupportBatchSize, clientset)
					writeLock.Unlock()
					//}
				} else {
					//log.Printf("deploy: no need to scale up and check to scale down function pods with vpod lottery= %d, FuncRealRps=%d for function %s......\n", funcConfig.ReqPerSecondLottery, funcDeployStatus.FuncRealRps, funcName)
					var deletedFuncPodConfig []*gTypes.FuncPodConfig
					for _, v := range funcDeployStatus.FuncPodConfigMap {
						if v.FuncPodType == "i" && v.ReqPerSecondLottery == 0 {
							if v.FuncPodName == funcDeployStatus.FuncPrewarmPodName {
								repository.UpdateFuncExpectedReplicas(funcName, funcDeployStatus.ExpectedReplicas-1)
								repository.UpdateFuncPodType(funcName, v.FuncPodName, "p")
								repository.UpdateFuncAvailReplicas(funcName, funcDeployStatus.AvailReplicas-1)
							} else {
								if v.InactiveCounter >= funcDeployStatus.FunctionInactiveNum {
									deletedFuncPodConfig = append(deletedFuncPodConfig, v)
								} else {
									repository.UpdateFuncPodInactiveCounter(funcName, v.FuncPodName, v.InactiveCounter+1)
								}
							}
						}
					}
					if len(deletedFuncPodConfig) > 0 {
						//if funcDeployStatus.FunctionIsScalingIn == false {
						//log.Printf("deploy: scaling in %d function pods for function %s......\n", len(deletedFuncPodConfig), funcName)

						writeLock.Lock()
						scheduler.ScaleDownFuncCapacity(funcName, namespace, deletedFuncPodConfig, clientset)
						writeLock.Unlock()

						repository.UpdateFuncLastChangedPodCombine(funcName, nil)
						//} else {
						//	log.Printf("deploy: waitting scaling in %d function pods for function %s......\n", len(deletedFuncPodConfig), funcName)
						//}
					} else {
						//log.Printf("deploy: no need to scale in %d function pods for function %s......\n", len(deletedFuncPodConfig), funcName)
					}
				}
			}
			continue
		case <-quit:
			return
		}
	}
	log.Printf("deploy: function scaling go rountine exits --------------------------- \n")
}
