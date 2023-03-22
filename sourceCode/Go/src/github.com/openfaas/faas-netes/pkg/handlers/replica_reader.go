// Copyright (c) Alex Ellis 2017. All rights reserved.
// Copyright 2020 OpenFaaS Author(s)
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package handlers

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/gorilla/mux"
	"github.com/openfaas/faas-netes/pkg/k8s"
	types "github.com/openfaas/faas-provider/types"
	"k8s.io/client-go/kubernetes"
	glog "k8s.io/klog"
)

// MakeReplicaReader reads the amount of replicas for a deployment
func MakeReplicaReader(defaultNamespace string, clientset *kubernetes.Clientset) http.HandlerFunc {
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

		s := time.Now()

		function, err := getService(lookupNamespace, functionName)
		if err != nil {
			log.Printf("Unable to fetch service: %s %s\n", functionName, namespace)
			w.WriteHeader(http.StatusInternalServerError)
			w.Write([]byte(err.Error()))
			return
		}

		if function == nil {
			w.WriteHeader(http.StatusNotFound)
			return
		}

		d := time.Since(s)
		log.Printf("Replicas: %s.%s, (%d/%d) %dms\n", functionName, lookupNamespace, function.AvailableReplicas, function.Replicas, d.Milliseconds())

		functionBytes, err := json.Marshal(function)
		if err != nil {
			glog.Errorf("Failed to marshal function: %s", err.Error())
			w.WriteHeader(http.StatusInternalServerError)
			w.Write([]byte("Failed to marshal function"))
			return
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusOK)
		w.Write(functionBytes)
	}
}

/**
 * getService returns a function/service or nil if not found
 * called by MakeReplicaReader()
 */

func getService(functionNamespace string, functionName string) (*types.FunctionStatus, error) {
	function := k8s.CreateFunctionPodStatus(functionName) // then read repository to get the pod information
	if function == nil {
		log.Printf("reader: error function'pod %s not found in repository while service still exists\n", functionName)
		return nil, fmt.Errorf("reader: error function'pod %s not found in repository while service still exists\n", functionName)
	} else {
		//log.Printf("reader: create a func status for function %s in namespace %s from repository, ExpectedReplicas= %d, AvailReplicas= %d \n", functionNamespace, functionName, function.Replicas, function.AvailableReplicas)
		return function, nil // create found
	}
}

// getService returns a function/service or nil if not found
/*func getService(functionNamespace string, functionName string, lister v1.DeploymentLister) (*types.FunctionStatus, error) {

	item, err := lister.Deployments(functionNamespace).
		Get(functionName)

	if err != nil {
		if errors.IsNotFound(err) {
			return nil, nil
		}

		return nil, err
	}

	if item != nil {
		function := k8s.AsFunctionStatus(*item)
		if function != nil {
			return function, nil
		}
	}

	return nil, fmt.Errorf("function: %s not found", functionName)
}*/
