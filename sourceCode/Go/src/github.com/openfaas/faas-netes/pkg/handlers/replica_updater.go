// Copyright (c) Alex Ellis 2017. All rights reserved.
// Copyright 2020 OpenFaaS Author(s)
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package handlers

import (
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/openfaas/faas-netes/gpu/repository"

	"k8s.io/client-go/kubernetes"
)

type ScaleServiceRequest struct {
	ServiceName      string `json:"serviceName"`
	ServiceNamespace string `json:"serviceNamespace"`
	Replicas         uint64 `json:"replicas"`
}

// MaxReplicas licensed for OpenFaaS CE is 5/5
const MaxReplicas = 5

/**
 * MakeReplicaUpdater updates desired count of replicas
 * For http calling for gateway cold start
 */
func MakeReplicaUpdater(defaultNamespace string, clientset *kubernetes.Clientset) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		log.Println("Update replicas")

		vars := mux.Vars(r)

		functionName := vars["name"]
		q := r.URL.Query()
		namespace := q.Get("namespace")

		lookupNamespace := defaultNamespace

		if len(namespace) > 0 {
			lookupNamespace = namespace
		}

		if lookupNamespace != defaultNamespace {
			http.Error(w, fmt.Sprintf("valid namespaces are: %s", defaultNamespace), http.StatusBadRequest)
			return
		}

		req := ScaleServiceRequest{}

		if r.Body != nil {
			defer r.Body.Close()
			bytesIn, _ := io.ReadAll(r.Body)
			marshalErr := json.Unmarshal(bytesIn, &req)
			if marshalErr != nil {
				w.WriteHeader(http.StatusBadRequest)
				msg := "Cannot parse request. Please pass valid JSON."
				w.Write([]byte(msg))
				log.Println(msg, marshalErr)
				return
			}
		}

		if len(req.ServiceNamespace) > 0 {
			lookupNamespace = req.ServiceNamespace
		}
		funcDeployStatus := repository.GetFunc(functionName)
		if funcDeployStatus == nil {
			w.WriteHeader(http.StatusInternalServerError)
			w.Write([]byte("replicas: Unable to lookup function deployment " + functionName + lookupNamespace))
			return
		}
		if funcDeployStatus.AvailReplicas == 0 { // no avail ipod to use
			/*minBatchSize := int32(9999)
			var funcConfigWithMinBatch *gTypes.FuncPodConfig
			for _ , funcConfig := range funcDeployStatus.FuncPodConfigMap { //find a ppod to conver into ipod
				if funcConfig.FuncPodType == "p" {
					if funcConfig.BatchSize < minBatchSize {
						minBatchSize = funcConfig.BatchSize
						funcConfigWithMinBatch = funcConfig
					}
				}
			}
			// todo: deal with the ppod
			if funcConfigWithMinBatch != nil {
				funcConfigWithMinBatch.FuncPodType = "i"
				repository.UpdateFuncPodConfig(functionName, funcConfigWithMinBatch)
				repository.UpdateFuncPodLottery(funcDeployStatus.FunctionName, funcConfigWithMinBatch.FuncPodName, funcConfigWithMinBatch.ReqPerSecondMax)
				funcConfig, exist := funcDeployStatus.FuncPodConfigMap["v"]
				if exist {
					repository.UpdateFuncPodLottery(funcDeployStatus.FunctionName, funcConfig.FuncPodName,0)
				}
			}*/
			for _, funcConfig := range funcDeployStatus.FuncPodConfigMap { //find a ppod to conver into ipod

				if funcConfig.FuncPodName == funcDeployStatus.FuncPrewarmPodName {
					repository.UpdateFuncExpectedReplicas(functionName, funcDeployStatus.ExpectedReplicas+1)
					//repository.UpdateFuncPrewarmPodName(functionName, funcConfig.FuncPodName)
					repository.UpdateFuncPodType(functionName, funcConfig.FuncPodName, "i")
					repository.UpdateFuncPodLottery(functionName, funcConfig.FuncPodName, funcConfig.ReqPerSecondMax)
					repository.UpdateFuncPodsTotalLotteryNoLog(functionName)
					repository.UpdateFuncAvailReplicas(functionName, funcDeployStatus.AvailReplicas+1)
					break
				}
			}
			funcConfig, exist := funcDeployStatus.FuncPodConfigMap["v"]
			if exist {
				repository.UpdateFuncPodLottery(funcDeployStatus.FunctionName, funcConfig.FuncPodName, 0)
			}

			//log.Printf("replicas: conver ppod to ipod of function %s to scale up ......\n", functionName)
		} else {
			log.Printf("replicas: function %s is in scaling up, no need to convert ppod to ipod ......\n", functionName)
		}

		w.WriteHeader(http.StatusAccepted)
		return
	}
}

/**
 * For alert calling, no http
 */
//func ExecReplicaUpdater(namespace string, functionName string, instanceConfig *gpuTypes.FuncPodConfig, requiredReplicas int32, clientset *kubernetes.Clientset) error {
//
//	funcDeployStatus := repository.GetFunc(functionName)
//	repository.UpdateFuncExpectedReplicas(functionName, funcDeployStatus.ExpectedReplicas+1)
//	if funcDeployStatus == nil {
//		log.Println("replicas: Unable to lookup function deployment " + functionName)
//		return fmt.Errorf("replicas: Unable to lookup function deployment " + functionName)
//	}
//
//	differ := funcDeployStatus.ExpectedReplicas - funcDeployStatus.AvailReplicas
//	if differ > 0 {
//		err := scaleUpFunc(funcDeployStatus, namespace, instanceConfig, funcDeployStatus.FuncPlaceConstraints, differ, clientset)
//		if err != nil {
//			log.Println(err.Error())
//			log.Println("replicas: Unable to scaleUp function deployment " + functionName)
//			return err
//		}
//	} else if differ < 0 {
//		err := scaleDownFunc(funcDeployStatus, namespace, -differ, clientset)
//		if err != nil {
//			log.Println(err.Error())
//			log.Println("replicas: Unable to scaleDown function deployment " + functionName)
//			return fmt.Errorf("replicas: Unable to scaleDown function deployment " + functionName)
//		}
//	} else {
//		//log.Println("replicas: ---------expectedReplicas=availReplicas do nothing-----------")
//		// expectedReplicas=availReplicas do nothing
//	}
//	return nil
//}

//func ExecReplicaUpdater(namespace string, functionName string, requiredReplicas int32, clientset *kubernetes.Clientset) error {
//	repository.UpdateFuncExpectedReplicas(functionName, requiredReplicas)
//
//	funcDeployStatus := repository.GetFunc(functionName)
//	if funcDeployStatus == nil {
//		log.Println("replicas: Unable to lookup function deployment " + functionName)
//		return fmt.Errorf("replicas: Unable to lookup function deployment " + functionName)
//	}
//	resourceLimits := &ptypes.FunctionResources {
//		Memory:     funcDeployStatus.FuncResources.Memory,
//		CPU:        funcDeployStatus.FuncResources.CPU,
//		GPU:        funcDeployStatus.FuncResources.GPU,
//		GPU_Memory: funcDeployStatus.FuncResources.GPU_Memory,
//	}
//
//	differ := funcDeployStatus.ExpectedReplicas - funcDeployStatus.AvailReplicas
//	if differ > 0 {
//		err := scaleUpFunc(funcDeployStatus, namespace, resourceLimits, funcDeployStatus.FuncConstraints, differ, clientset)
//		if err != nil {
//			log.Println(err.Error())
//			log.Println("replicas: Unable to scaleUp function deployment " + functionName)
//			return err
//		}
//	} else if differ < 0 {
//		err := scaleDownFunc(funcDeployStatus, namespace, -differ, clientset)
//		if err != nil {
//			log.Println(err.Error())
//			log.Println("replicas: Unable to scaleDown function deployment " + functionName)
//			return fmt.Errorf("replicas: Unable to scaleDown function deployment " + functionName)
//		}
//	} else {
//		//log.Println("replicas: ---------expectedReplicas=availReplicas do nothing-----------")
//		// expectedReplicas=availReplicas do nothing
//	}
//	return nil
//}
//func scaleUpFunc(funcDeployStatus *gpuTypes.FuncDeployStatus, namespace string, resourceLimits *ptypes.FunctionResources, nodeConstrains []string, differ int32, clientset *kubernetes.Clientset) error{
//	nodeSelector := map[string]string{} // init=map{}
//	var nodeGpuAlloc *gpuTypes.NodeGpuCpuAllocation // init=nil
//
//	// there is need to decide the new pod's name and deploy node
//	for i := int32(0); i < differ; i++ {
//		//start := time.Now()
//		nodeGpuAlloc = scheduler.FindGpuDeployNode(resourceLimits, nodeConstrains) // only for GPU and GPU_Memory
//		if nodeGpuAlloc == nil || nodeGpuAlloc.NodeIndex == -1 {
//			log.Println("replicas: no available node in cluster for scale up")
//			return fmt.Errorf("replicas: no available node in cluster for scale up")
//		}
//		// build the node selector
//		nodeLabelStrList := strings.Split(repository.GetClusterCapConfig().ClusterCapacity[nodeGpuAlloc.NodeIndex].NodeLabel, "=")
//		nodeSelector[nodeLabelStrList[0]] = nodeLabelStrList[1]
//		// build the cuda device env str
//		cudaDeviceIndexEnvStr := strconv.Itoa(nodeGpuAlloc.CudaDeviceIndex)
//		if len(funcDeployStatus.FuncSpec.Pod.Spec.Containers)==0 {
//			return fmt.Errorf("replicas: funcSpec.pod.spec.container's length=0 error")
//		}
//		envItemSize := len(funcDeployStatus.FuncSpec.Pod.Spec.Containers[0].Env)
//		for j := 0; j < envItemSize; j++ {
//			if funcDeployStatus.FuncSpec.Pod.Spec.Containers[0].Env[j].Name == "CUDA_VISIBLE_DEVICES" {
//				funcDeployStatus.FuncSpec.Pod.Spec.Containers[0].Env[j].Value = cudaDeviceIndexEnvStr
//				break
//			}
//		}
//		for j := 0; j < envItemSize; j++ {
//			if funcDeployStatus.FuncSpec.Pod.Spec.Containers[0].Env[j].Name == "GPU_MEM_FRACTION" {
//				funcDeployStatus.FuncSpec.Pod.Spec.Containers[0].Env[j].Value = funcDeployStatus.FuncResources.GPU_Memory
//				break
//			}
//		}
//
//		funcDeployStatus.FuncSpec.Pod.Name = funcDeployStatus.FunctionName + "-pod-n" + strconv.Itoa(nodeGpuAlloc.NodeIndex) +"g"+ strconv.Itoa(nodeGpuAlloc.CudaDeviceIndex) + "-" +tools.RandomText(8)
//		funcDeployStatus.FuncSpec.Pod.Spec.NodeSelector = nodeSelector
//		_, err := clientset.CoreV1().Pods(namespace).Create(funcDeployStatus.FuncSpec.Pod)
//		if err != nil {
//			wrappedErr := fmt.Errorf("replicas: scaleup function %s 's Pod for differ %d error: %s \n", funcDeployStatus.FunctionName, i+1, err.Error())
//			log.Println(wrappedErr)
//			return err
//		}
//		//log.Printf("replicas: scale function %s took: %fs \n", funcDeployStatus.FunctionName, time.Since(start).Seconds())
//
//		/**
//		 * allocate cpu core
//		 */
//		if funcDeployStatus != nil && funcDeployStatus.FuncCpuCoreBind != ""{
//			coreBindErr := cpuRepository.AssignPodToCpuCore(clientset, funcDeployStatus.FuncSpec.Pod.Name, nodeGpuAlloc.NodeIndex, funcDeployStatus.FuncCpuCoreBind)
//			if coreBindErr != nil {
//				log.Println(coreBindErr.Error())
//				return fmt.Errorf(coreBindErr.Error())
//			}
//			nodeGpuAlloc.CpuCoreIdStr = funcDeployStatus.FuncCpuCoreBind
//		}
//		//log.Printf("replicas: scaleup function %s 's Pod for differ %d successfully \n", funcDeployStatus.FunctionName, i+1)
//		repository.UpdateFuncAvailReplicas(funcDeployStatus.FunctionName, funcDeployStatus.AvailReplicas+1)
//		funcPodConfig := gpuTypes.FuncPodConfig{
//			FuncPodName:          "",
//			BatchSize:            0,
//			CpuThreads:           0,
//			GpuCorePercent:       0,
//			ExecutionTime:        0,
//			ReqPerSecond:         0,
//			FuncPodIp:            "",
//			NodeGpuCpuAllocation: nodeGpuAlloc,
//		}
//		repository.UpdateFuncPodConfig(funcDeployStatus.FunctionName, funcDeployStatus.FuncSpec.Pod.Name, &funcPodConfig)
//	}
//	return nil
//}
//func scaleDownFunc(funcDeployStatus *gpuTypes.FuncDeployStatus, namespace string, differ int32, clientset *kubernetes.Clientset) error{
//	if funcDeployStatus.AvailReplicas < differ {
//		log.Printf("replicas: function %s does not has enough instances %d for differ %d \n", funcDeployStatus.FunctionName, funcDeployStatus.AvailReplicas, differ)
//		return fmt.Errorf("replicas: function %s does not has enough instances %d for differ %d \n", funcDeployStatus.FunctionName, funcDeployStatus.AvailReplicas, differ)
//	}
//	foregroundPolicy := metav1.DeletePropagationForeground
//	opts := &metav1.DeleteOptions{PropagationPolicy: &foregroundPolicy}
//
//	for i := int32(0); i < differ; i++ {
//		//start := time.Now()
//		podName := scheduler.FindGpuDeletePod(funcDeployStatus)
//		err := clientset.CoreV1().Pods(namespace).Delete(podName, opts)
//		if err != nil {
//			log.Printf("replicas: function %s deleted pod %s error \n", funcDeployStatus.FunctionName, podName)
//			return err
//		}
//		//log.Printf("replicas: function %s deleted pod %s successfully \n", funcDeployStatus.FunctionName, podName)
//		repository.UpdateFuncAvailReplicas(funcDeployStatus.FunctionName, funcDeployStatus.AvailReplicas-1)
//		repository.DeleteFuncPodLocation(funcDeployStatus.FunctionName, podName)
//		//log.Printf("replicas: scale function %s took: %fs \n", funcDeployStatus.FunctionName, time.Since(start).Seconds())
//
//	}
//
//	return nil
//}

// MakeReplicaUpdater updates desired count of replicas
/*func MakeReplicaUpdater(defaultNamespace string, clientset *kubernetes.Clientset) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		log.Println("Update replicas")

		vars := mux.Vars(r)

		functionName := vars["name"]
		q := r.URL.Query()
		namespace := q.Get("namespace")

		lookupNamespace := defaultNamespace

		if len(namespace) > 0 {
			lookupNamespace = namespace
		}

		if lookupNamespace != defaultNamespace {
			http.Error(w, fmt.Sprintf("valid namespaces are: %s", defaultNamespace), http.StatusBadRequest)
			return
		}

		req := ScaleServiceRequest{}

		if r.Body != nil {
			defer r.Body.Close()
			bytesIn, _ := io.ReadAll(r.Body)
			marshalErr := json.Unmarshal(bytesIn, &req)
			if marshalErr != nil {
				w.WriteHeader(http.StatusBadRequest)
				msg := "Cannot parse request. Please pass valid JSON."
				w.Write([]byte(msg))
				log.Println(msg, marshalErr)
				return
			}
		}

		if req.Replicas == 0 {
			http.Error(w, "replicas cannot be set to 0 in OpenFaaS CE",
				http.StatusBadRequest)
			return
		}

		options := metav1.GetOptions{
			TypeMeta: metav1.TypeMeta{
				Kind:       "Deployment",
				APIVersion: "apps/v1",
			},
		}

		deployment, err := clientset.AppsV1().Deployments(lookupNamespace).Get(context.TODO(), functionName, options)

		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			w.Write([]byte("Unable to lookup function deployment " + functionName))
			log.Println(err)
			return
		}

		oldReplicas := *deployment.Spec.Replicas
		replicas := int32(req.Replicas)
		if replicas >= MaxReplicas {
			replicas = MaxReplicas
		}

		log.Printf("Set replicas - %s %s, %d/%d\n", functionName, lookupNamespace, replicas, oldReplicas)

		deployment.Spec.Replicas = &replicas

		if _, err = clientset.AppsV1().Deployments(lookupNamespace).
			Update(context.TODO(), deployment, metav1.UpdateOptions{}); err != nil {

			log.Printf("unable to update function deployment: %s, %s", functionName, err)
			http.Error(w, fmt.Sprintf("unable to update function deployment: %s", functionName), http.StatusInternalServerError)
			return
		}

		w.WriteHeader(http.StatusAccepted)
	}
}*/
