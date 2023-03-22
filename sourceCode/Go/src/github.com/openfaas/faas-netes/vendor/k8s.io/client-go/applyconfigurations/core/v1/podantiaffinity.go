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

package v1

// PodAntiAffinityApplyConfiguration represents an declarative configuration of the PodAntiAffinity type for use
// with apply.
type PodAntiAffinityApplyConfiguration struct {
	RequiredDuringSchedulingIgnoredDuringExecution  []PodAffinityTermApplyConfiguration         `json:"requiredDuringSchedulingIgnoredDuringExecution,omitempty"`
	PreferredDuringSchedulingIgnoredDuringExecution []WeightedPodAffinityTermApplyConfiguration `json:"preferredDuringSchedulingIgnoredDuringExecution,omitempty"`
}

// PodAntiAffinityApplyConfiguration constructs an declarative configuration of the PodAntiAffinity type for use with
// apply.
func PodAntiAffinity() *PodAntiAffinityApplyConfiguration {
	return &PodAntiAffinityApplyConfiguration{}
}

// WithRequiredDuringSchedulingIgnoredDuringExecution adds the given value to the RequiredDuringSchedulingIgnoredDuringExecution field in the declarative configuration
// and returns the receiver, so that objects can be build by chaining "With" function invocations.
// If called multiple times, values provided by each call will be appended to the RequiredDuringSchedulingIgnoredDuringExecution field.
func (b *PodAntiAffinityApplyConfiguration) WithRequiredDuringSchedulingIgnoredDuringExecution(values ...*PodAffinityTermApplyConfiguration) *PodAntiAffinityApplyConfiguration {
	for i := range values {
		if values[i] == nil {
			panic("nil value passed to WithRequiredDuringSchedulingIgnoredDuringExecution")
		}
		b.RequiredDuringSchedulingIgnoredDuringExecution = append(b.RequiredDuringSchedulingIgnoredDuringExecution, *values[i])
	}
	return b
}

// WithPreferredDuringSchedulingIgnoredDuringExecution adds the given value to the PreferredDuringSchedulingIgnoredDuringExecution field in the declarative configuration
// and returns the receiver, so that objects can be build by chaining "With" function invocations.
// If called multiple times, values provided by each call will be appended to the PreferredDuringSchedulingIgnoredDuringExecution field.
func (b *PodAntiAffinityApplyConfiguration) WithPreferredDuringSchedulingIgnoredDuringExecution(values ...*WeightedPodAffinityTermApplyConfiguration) *PodAntiAffinityApplyConfiguration {
	for i := range values {
		if values[i] == nil {
			panic("nil value passed to WithPreferredDuringSchedulingIgnoredDuringExecution")
		}
		b.PreferredDuringSchedulingIgnoredDuringExecution = append(b.PreferredDuringSchedulingIgnoredDuringExecution, *values[i])
	}
	return b
}
