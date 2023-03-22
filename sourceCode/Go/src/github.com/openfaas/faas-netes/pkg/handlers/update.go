// Copyright 2020 OpenFaaS Author(s)
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package handlers

import (
	"context"
	"fmt"

	"log"
	"net/http"
	"time"

	"github.com/openfaas/faas-netes/gpu/repository"
	"github.com/openfaas/faas-netes/pkg/k8s"
	"k8s.io/apimachinery/pkg/labels"

	types "github.com/openfaas/faas-provider/types"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
)

// MakeUpdateHandler update specified function
func MakeUpdateHandler(defaultNamespace string, factory k8s.FunctionFactory) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		/*ctx := r.Context()
		if r.Body != nil {
			defer r.Body.Close()
		}

		body, _ := io.ReadAll(r.Body)

		request := types.FunctionDeployment{}
		err := json.Unmarshal(body, &request)
		if err != nil {
			wrappedErr := fmt.Errorf("unable to unmarshal request: %s", err.Error())
			http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
			return
		}

		if err := ValidateDeployRequest(&request); err != nil {
			wrappedErr := fmt.Errorf("validation failed: %s", err.Error())
			http.Error(w, wrappedErr.Error(), http.StatusBadRequest)
			return
		}

		lookupNamespace := defaultNamespace
		if len(request.Namespace) > 0 {
			lookupNamespace = request.Namespace
		}

		if lookupNamespace != defaultNamespace {
			http.Error(w, fmt.Sprintf("valid namespaces are: %s", defaultNamespace), http.StatusBadRequest)
			return
		}

		if lookupNamespace == "kube-system" {
			http.Error(w, "unable to list within the kube-system namespace", http.StatusUnauthorized)
			return
		}

		annotations, err := buildAnnotations(request)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}

		if err, status := updateDeploymentSpec(ctx, lookupNamespace, factory, request, annotations); err != nil {
			if !k8s.IsNotFound(err) {
				log.Printf("error updating deployment: %s.%s, error: %s\n", request.Service, lookupNamespace, err)

				return
			}

			wrappedErr := fmt.Errorf("unable update Deployment: %s.%s, error: %s", request.Service, lookupNamespace, err.Error())
			http.Error(w, wrappedErr.Error(), status)
			return
		}

		if err, status := updateService(lookupNamespace, factory, request, annotations); err != nil {
			if !k8s.IsNotFound(err) {
				log.Printf("error updating service: %s.%s, error: %s\n", request.Service, lookupNamespace, err)
			}

			wrappedErr := fmt.Errorf("unable update Service: %s.%s, error: %s", request.Service, request.Namespace, err.Error())
			http.Error(w, wrappedErr.Error(), status)
			return
		}*/

		w.WriteHeader(http.StatusAccepted)
	}
}

func updatePodSpec(
	functionNamespace string,
	factory k8s.FunctionFactory,
	request types.FunctionDeployment,
	annotations map[string]string) (err error, httpStatus int) {

	labelPod := labels.SelectorFromSet(map[string]string{"faas_function": request.Service})
	listPodOptions := metav1.ListOptions{
		LabelSelector: labelPod.String(),
	}
	// This makes sure we don't delete non-labeled deployments
	podList, findPodsErr := factory.Client.CoreV1().Pods(functionNamespace).List(context.TODO(), listPodOptions)
	if findPodsErr != nil {
		return findPodsErr, http.StatusNotFound
	}

	for i := 0; i < len(podList.Items); i++ {
		if len(podList.Items[i].Spec.Containers) > 0 {
			podList.Items[i].Spec.Containers[0].Image = request.Image

			// Disabling update support to prevent unexpected mutations of deployed functions,
			// since imagePullPolicy is now configurable. This could be reconsidered later depending
			// on desired behavior, but will need to be updated to take config.
			//deployment.Spec.Template.Spec.Containers[0].ImagePullPolicy = v1.PullAlways

			podList.Items[i].Spec.Containers[0].Env = buildEnvVars(&request)

			factory.ConfigureReadOnlyRootFilesystem(request, &podList.Items[i])
			factory.ConfigureContainerUserID(&podList.Items[i])

			podList.Items[i].Spec.NodeSelector = createSelector(request.Constraints)

			label := map[string]string{
				"faas_function": request.Service,
				"uid":           fmt.Sprintf("%d", time.Now().Nanosecond()),
			}

			if request.Labels != nil {
				for k, v := range *request.Labels {
					label[k] = v
				}
			}

			// deployment.Labels = labels
			podList.Items[i].ObjectMeta.Labels = label
			podList.Items[i].ObjectMeta.Annotations = annotations

			resources, resourceErr := createResources(request)
			if resourceErr != nil {
				return resourceErr, http.StatusBadRequest
			}

			podList.Items[i].Spec.Containers[0].Resources = *resources

			var serviceAccount string

			if request.Annotations != nil {
				annotations := *request.Annotations
				if val, ok := annotations["com.openfaas.serviceaccount"]; ok && len(val) > 0 {
					serviceAccount = val
				}
			}
			podList.Items[i].Spec.ServiceAccountName = serviceAccount

			secrets := k8s.NewSecretsClient(factory.Client)
			existingSecrets, err := secrets.GetSecrets(functionNamespace, request.Secrets)
			if err != nil {
				return err, http.StatusBadRequest
			}

			err = factory.ConfigureSecrets(request, &podList.Items[i], existingSecrets)
			if err != nil {
				log.Println(err)
				return err, http.StatusBadRequest
			}

			probes, err := factory.MakeProbes(request)
			if err != nil {
				return err, http.StatusBadRequest
			}

			podList.Items[i].Spec.Containers[0].LivenessProbe = probes.Liveness
			podList.Items[i].Spec.Containers[0].ReadinessProbe = probes.Readiness
		}

		if _, updateErr := factory.Client.CoreV1().
			Pods(functionNamespace).
			Update(context.TODO(), &podList.Items[i], metav1.UpdateOptions{}); updateErr != nil {

			return updateErr, http.StatusInternalServerError
		}
	}
	if len(podList.Items) > 0 {
		repository.UpdateFuncSpec(request.Service, &podList.Items[0], nil)
		repository.UpdateFuncConstrains(request.Service, request.Constraints)
		repository.UpdateFuncRequestResources(request.Service, request.Requests)
	}
	return nil, http.StatusAccepted
}

/*func updateDeploymentSpec(
	ctx context.Context,
	functionNamespace string,
	factory k8s.FunctionFactory,
	request types.FunctionDeployment,
	annotations map[string]string) (err error, httpStatus int) {

	getOpts := metav1.GetOptions{}

	deployment, findDeployErr := factory.Client.AppsV1().
		Deployments(functionNamespace).
		Get(context.TODO(), request.Service, getOpts)

	if findDeployErr != nil {
		return findDeployErr, http.StatusNotFound
	}

	if len(deployment.Spec.Template.Spec.Containers) > 0 {
		deployment.Spec.Template.Spec.Containers[0].Image = request.Image

		// Disabling update support to prevent unexpected mutations of deployed functions,
		// since imagePullPolicy is now configurable. This could be reconsidered later depending
		// on desired behavior, but will need to be updated to take config.
		//deployment.Spec.Template.Spec.Containers[0].ImagePullPolicy = v1.PullAlways

		deployment.Spec.Template.Spec.Containers[0].Env = buildEnvVars(&request)

		factory.ConfigureReadOnlyRootFilesystem(request, deployment)
		factory.ConfigureContainerUserID(deployment)

		deployment.Spec.Template.Spec.NodeSelector = createSelector(request.Constraints)

		labels := map[string]string{
			"faas_function": request.Service,
			"uid":           fmt.Sprintf("%d", time.Now().Nanosecond()),
		}

		if request.Labels != nil {
			if min := getMinReplicaCount(*request.Labels); min != nil {
				deployment.Spec.Replicas = min
			}

			for k, v := range *request.Labels {
				labels[k] = v
			}
		}

		// deployment.Labels = labels
		deployment.Spec.Template.ObjectMeta.Labels = labels

		// store the current annotations so that we can diff the annotations
		// and determine which profiles need to be removed
		currentAnnotations := deployment.Annotations
		deployment.Annotations = annotations
		deployment.Spec.Template.Annotations = annotations
		deployment.Spec.Template.ObjectMeta.Annotations = annotations

		resources, resourceErr := createResources(request)
		if resourceErr != nil {
			return resourceErr, http.StatusBadRequest
		}

		deployment.Spec.Template.Spec.Containers[0].Resources = *resources

		secrets := k8s.NewSecretsClient(factory.Client)
		existingSecrets, err := secrets.GetSecrets(functionNamespace, request.Secrets)
		if err != nil {
			return err, http.StatusBadRequest
		}

		err = factory.ConfigureSecrets(request, deployment, existingSecrets)
		if err != nil {
			log.Println(err)
			return err, http.StatusBadRequest
		}

		probes, err := factory.MakeProbes(request)
		if err != nil {
			return err, http.StatusBadRequest
		}

		deployment.Spec.Template.Spec.Containers[0].LivenessProbe = probes.Liveness
		deployment.Spec.Template.Spec.Containers[0].ReadinessProbe = probes.Readiness

		// compare the annotations from args to the cache copy of the deployment annotations
		// at this point we have already updated the annotations to the new value, if we
		// compare to that it will produce an empty list
		profileNamespace := factory.Config.ProfilesNamespace
		profileList, err := factory.GetProfilesToRemove(ctx, profileNamespace, annotations, currentAnnotations)
		if err != nil {
			return err, http.StatusBadRequest
		}
		for _, profile := range profileList {
			factory.RemoveProfile(profile, deployment)
		}

		profileList, err = factory.GetProfiles(ctx, profileNamespace, annotations)
		if err != nil {
			return err, http.StatusBadRequest
		}
		for _, profile := range profileList {
			factory.ApplyProfile(profile, deployment)
		}
	}

	if _, updateErr := factory.Client.AppsV1().
		Deployments(functionNamespace).
		Update(context.TODO(), deployment, metav1.UpdateOptions{}); updateErr != nil {

		return updateErr, http.StatusInternalServerError
	}

	return nil, http.StatusAccepted
}*/

func updateService(
	functionNamespace string,
	factory k8s.FunctionFactory,
	request types.FunctionDeployment,
	annotations map[string]string) (err error, httpStatus int) {

	getOpts := metav1.GetOptions{}

	service, findServiceErr := factory.Client.CoreV1().
		Services(functionNamespace).
		Get(context.TODO(), request.Service, getOpts)

	if findServiceErr != nil {
		return findServiceErr, http.StatusNotFound
	}
	if service.Name == "" {
		return nil, http.StatusAccepted
	}
	service.Annotations = annotations

	if _, updateErr := factory.Client.CoreV1().
		Services(functionNamespace).
		Update(context.TODO(), service, metav1.UpdateOptions{}); updateErr != nil {

		return updateErr, http.StatusInternalServerError
	}

	repository.UpdateFuncSpec(request.Service, nil, service)
	return nil, http.StatusAccepted
}
