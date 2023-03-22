// Copyright (c) Alex Ellis 2017. All rights reserved.
// Copyright 2020 OpenFaaS Author(s)
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package handlers

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	types "github.com/openfaas/faas-provider/types"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/client-go/kubernetes"
	glog "k8s.io/klog"

	"github.com/openfaas/faas-netes/pkg/k8s"
)

// MakeFunctionReader handler for reading functions deployed in the cluster as deployments.
func MakeFunctionReader(defaultNamespace string, clientset *kubernetes.Clientset) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {

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

		if lookupNamespace == "kube-system" {
			http.Error(w, "unable to list within the kube-system namespace", http.StatusUnauthorized)
			return
		}

		functions, err := getServiceList(lookupNamespace, clientset)
		if err != nil {
			log.Println(err)
			w.WriteHeader(http.StatusInternalServerError)
			w.Write([]byte(err.Error()))
			return
		}

		functionBytes, err := json.Marshal(functions)
		if err != nil {
			glog.Errorf("Failed to marshal functions: %s", err.Error())
			w.WriteHeader(http.StatusInternalServerError)
			w.Write([]byte("Failed to marshal functions"))
			return
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write(functionBytes)
	}
}

/**
 * invoked by makeFunctionReader
 */
func getServiceList(functionNamespace string, clientset *kubernetes.Clientset) ([]types.FunctionStatus, error) {
	var functions []types.FunctionStatus // init = nil
	var function *types.FunctionStatus   // init = nil

	// search service firstly
	listOpts := metav1.ListOptions{}
	srvList, srvErr := clientset.CoreV1().Services(functionNamespace).List(context.TODO(), listOpts)
	if srvErr != nil {
		log.Println(srvErr.Error())
		return nil, fmt.Errorf("reader: funtions's services in namespace %s list error \n", functionNamespace)
	}
	for _, srvItem := range srvList.Items {
		function = k8s.CreateFunctionPodStatus(srvItem.Name) // then read repository to get the pod information
		if function == nil {
			log.Printf("reader: error function'pod %s not found in repository while service still exists\n", srvItem.Name)
			return nil, fmt.Errorf("reader: error function'pod %s not found in repository while service still exists\n", srvItem.Name)
		} else {
			//log.Printf("reader: create a func status for function %s in namespace %s from repository, ExpectedReplicas= %d, AvailReplicas= %d \n", functionNamespace, srvItem.Name, function.Replicas, function.AvailableReplicas)
			functions = append(functions, *function)
		}
	}
	//for _, srvItem := range srvList.Items {
	//	// search pod secondly
	//	listOpts.LabelSelector = "faas_function=" + srvItem.Name
	//	podsList, podErr := clientset.CoreV1().Pods(functionNamespace).List(listOpts)
	//	if podErr != nil {
	//		log.Println(podErr.Error())
	//		return nil, fmt.Errorf("reader: funtions's pods in namespace %s list error \n", functionNamespace)
	//	}
	//	if len(podsList.Items) > 0 {
	//		function = k8s.AsFunctionPodStatus(srvItem.Name, podsList.Items[0])
	//	}
	//	if function == nil { //means service exists, but pod has been scaled in to 0
	//		log.Printf("reader: service %s exists in namespace=%s, but pod has been scaled in to 0 \n",srvItem.Name, functionNamespace)
	//		function = k8s.CreateFunctionPodStatus(srvItem.Name) // then read repository to get the pod information
	//		if function == nil {
	//			log.Printf("reader: error function'pod %s not found in repository while service still exists\n", srvItem.Name)
	//			return nil, fmt.Errorf("reader: error function'pod %s not found in repository while service still exists\n", srvItem.Name)
	//		} else {
	//			log.Printf("reader: create a func status for function %s in namespace %s from repository, ExpectedReplicas= %d, AvailReplicas= %d \n", functionNamespace, srvItem.Name, function.Replicas, function.AvailableReplicas)
	//			functions = append(functions, *function)
	//		}
	//	} else {
	//		log.Printf("reader: as a func status for function %s in namespace %s from repository, ExpectedReplicas= %d, AvailReplicas= %d \n", functionNamespace, srvItem.Name, function.Replicas, function.AvailableReplicas)
	//		functions = append(functions, *function)
	//	}
	//}
	return functions, nil
}

/*func getServiceList(functionNamespace string, deploymentLister v1.DeploymentLister) ([]types.FunctionStatus, error) {
	functions := []types.FunctionStatus{}

	sel := labels.NewSelector()
	req, err := labels.NewRequirement("faas_function", selection.Exists, []string{})
	if err != nil {
		return functions, err
	}
	onlyFunctions := sel.Add(*req)

	res, err := deploymentLister.Deployments(functionNamespace).List(onlyFunctions)

	if err != nil {
		return nil, err
	}

	for _, item := range res {
		if item != nil {
			function := k8s.AsFunctionStatus(*item)
			if function != nil {
				functions = append(functions, *function)
			}
		}
	}

	return functions, nil
}*/
