/*
Copyright The Kubernetes Authors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

// Code generated by applyconfiguration-gen. DO NOT EDIT.

package v1beta2

// ReplicaSetStatusApplyConfiguration represents an declarative configuration of the ReplicaSetStatus type for use
// with apply.
type ReplicaSetStatusApplyConfiguration struct {
	Replicas             *int32                                  `json:"replicas,omitempty"`
	FullyLabeledReplicas *int32                                  `json:"fullyLabeledReplicas,omitempty"`
	ReadyReplicas        *int32                                  `json:"readyReplicas,omitempty"`
	AvailableReplicas    *int32                                  `json:"availableReplicas,omitempty"`
	ObservedGeneration   *int64                                  `json:"observedGeneration,omitempty"`
	Conditions           []ReplicaSetConditionApplyConfiguration `json:"conditions,omitempty"`
}

// ReplicaSetStatusApplyConfiguration constructs an declarative configuration of the ReplicaSetStatus type for use with
// apply.
func ReplicaSetStatus() *ReplicaSetStatusApplyConfiguration {
	return &ReplicaSetStatusApplyConfiguration{}
}

// WithReplicas sets the Replicas field in the declarative configuration to the given value
// and returns the receiver, so that objects can be built by chaining "With" function invocations.
// If called multiple times, the Replicas field is set to the value of the last call.
func (b *ReplicaSetStatusApplyConfiguration) WithReplicas(value int32) *ReplicaSetStatusApplyConfiguration {
	b.Replicas = &value
	return b
}

// WithFullyLabeledReplicas sets the FullyLabeledReplicas field in the declarative configuration to the given value
// and returns the receiver, so that objects can be built by chaining "With" function invocations.
// If called multiple times, the FullyLabeledReplicas field is set to the value of the last call.
func (b *ReplicaSetStatusApplyConfiguration) WithFullyLabeledReplicas(value int32) *ReplicaSetStatusApplyConfiguration {
	b.FullyLabeledReplicas = &value
	return b
}

// WithReadyReplicas sets the ReadyReplicas field in the declarative configuration to the given value
// and returns the receiver, so that objects can be built by chaining "With" function invocations.
// If called multiple times, the ReadyReplicas field is set to the value of the last call.
func (b *ReplicaSetStatusApplyConfiguration) WithReadyReplicas(value int32) *ReplicaSetStatusApplyConfiguration {
	b.ReadyReplicas = &value
	return b
}

// WithAvailableReplicas sets the AvailableReplicas field in the declarative configuration to the given value
// and returns the receiver, so that objects can be built by chaining "With" function invocations.
// If called multiple times, the AvailableReplicas field is set to the value of the last call.
func (b *ReplicaSetStatusApplyConfiguration) WithAvailableReplicas(value int32) *ReplicaSetStatusApplyConfiguration {
	b.AvailableReplicas = &value
	return b
}

// WithObservedGeneration sets the ObservedGeneration field in the declarative configuration to the given value
// and returns the receiver, so that objects can be built by chaining "With" function invocations.
// If called multiple times, the ObservedGeneration field is set to the value of the last call.
func (b *ReplicaSetStatusApplyConfiguration) WithObservedGeneration(value int64) *ReplicaSetStatusApplyConfiguration {
	b.ObservedGeneration = &value
	return b
}

// WithConditions adds the given value to the Conditions field in the declarative configuration
// and returns the receiver, so that objects can be build by chaining "With" function invocations.
// If called multiple times, values provided by each call will be appended to the Conditions field.
func (b *ReplicaSetStatusApplyConfiguration) WithConditions(values ...*ReplicaSetConditionApplyConfiguration) *ReplicaSetStatusApplyConfiguration {
	for i := range values {
		if values[i] == nil {
			panic("nil value passed to WithConditions")
		}
		b.Conditions = append(b.Conditions, *values[i])
	}
	return b
}
