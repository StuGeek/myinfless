// +build !ignore_autogenerated

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

// Code generated by prerelease-lifecycle-gen. DO NOT EDIT.

package v1beta1

import (
	schema "k8s.io/apimachinery/pkg/runtime/schema"
)

// APILifecycleIntroduced is an autogenerated function, returning the release in which the API struct was introduced as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:introduced" tags in types.go.
func (in *ClusterRole) APILifecycleIntroduced() (major, minor int) {
	return 1, 6
}

// APILifecycleDeprecated is an autogenerated function, returning the release in which the API struct was or will be deprecated as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:deprecated" tags in types.go or  "k8s:prerelease-lifecycle-gen:introduced" plus three minor.
func (in *ClusterRole) APILifecycleDeprecated() (major, minor int) {
	return 1, 17
}

// APILifecycleReplacement is an autogenerated function, returning the group, version, and kind that should be used instead of this deprecated type.
// It is controlled by "k8s:prerelease-lifecycle-gen:replacement=<group>,<version>,<kind>" tags in types.go.
func (in *ClusterRole) APILifecycleReplacement() schema.GroupVersionKind {
	return schema.GroupVersionKind{Group: "rbac.authorization.k8s.io", Version: "v1", Kind: "ClusterRole"}
}

// APILifecycleRemoved is an autogenerated function, returning the release in which the API is no longer served as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:removed" tags in types.go or  "k8s:prerelease-lifecycle-gen:deprecated" plus three minor.
func (in *ClusterRole) APILifecycleRemoved() (major, minor int) {
	return 1, 22
}

// APILifecycleIntroduced is an autogenerated function, returning the release in which the API struct was introduced as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:introduced" tags in types.go.
func (in *ClusterRoleBinding) APILifecycleIntroduced() (major, minor int) {
	return 1, 6
}

// APILifecycleDeprecated is an autogenerated function, returning the release in which the API struct was or will be deprecated as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:deprecated" tags in types.go or  "k8s:prerelease-lifecycle-gen:introduced" plus three minor.
func (in *ClusterRoleBinding) APILifecycleDeprecated() (major, minor int) {
	return 1, 17
}

// APILifecycleReplacement is an autogenerated function, returning the group, version, and kind that should be used instead of this deprecated type.
// It is controlled by "k8s:prerelease-lifecycle-gen:replacement=<group>,<version>,<kind>" tags in types.go.
func (in *ClusterRoleBinding) APILifecycleReplacement() schema.GroupVersionKind {
	return schema.GroupVersionKind{Group: "rbac.authorization.k8s.io", Version: "v1", Kind: "ClusterRoleBinding"}
}

// APILifecycleRemoved is an autogenerated function, returning the release in which the API is no longer served as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:removed" tags in types.go or  "k8s:prerelease-lifecycle-gen:deprecated" plus three minor.
func (in *ClusterRoleBinding) APILifecycleRemoved() (major, minor int) {
	return 1, 22
}

// APILifecycleIntroduced is an autogenerated function, returning the release in which the API struct was introduced as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:introduced" tags in types.go.
func (in *ClusterRoleBindingList) APILifecycleIntroduced() (major, minor int) {
	return 1, 6
}

// APILifecycleDeprecated is an autogenerated function, returning the release in which the API struct was or will be deprecated as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:deprecated" tags in types.go or  "k8s:prerelease-lifecycle-gen:introduced" plus three minor.
func (in *ClusterRoleBindingList) APILifecycleDeprecated() (major, minor int) {
	return 1, 17
}

// APILifecycleReplacement is an autogenerated function, returning the group, version, and kind that should be used instead of this deprecated type.
// It is controlled by "k8s:prerelease-lifecycle-gen:replacement=<group>,<version>,<kind>" tags in types.go.
func (in *ClusterRoleBindingList) APILifecycleReplacement() schema.GroupVersionKind {
	return schema.GroupVersionKind{Group: "rbac.authorization.k8s.io", Version: "v1", Kind: "ClusterRoleBindingList"}
}

// APILifecycleRemoved is an autogenerated function, returning the release in which the API is no longer served as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:removed" tags in types.go or  "k8s:prerelease-lifecycle-gen:deprecated" plus three minor.
func (in *ClusterRoleBindingList) APILifecycleRemoved() (major, minor int) {
	return 1, 22
}

// APILifecycleIntroduced is an autogenerated function, returning the release in which the API struct was introduced as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:introduced" tags in types.go.
func (in *ClusterRoleList) APILifecycleIntroduced() (major, minor int) {
	return 1, 6
}

// APILifecycleDeprecated is an autogenerated function, returning the release in which the API struct was or will be deprecated as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:deprecated" tags in types.go or  "k8s:prerelease-lifecycle-gen:introduced" plus three minor.
func (in *ClusterRoleList) APILifecycleDeprecated() (major, minor int) {
	return 1, 17
}

// APILifecycleReplacement is an autogenerated function, returning the group, version, and kind that should be used instead of this deprecated type.
// It is controlled by "k8s:prerelease-lifecycle-gen:replacement=<group>,<version>,<kind>" tags in types.go.
func (in *ClusterRoleList) APILifecycleReplacement() schema.GroupVersionKind {
	return schema.GroupVersionKind{Group: "rbac.authorization.k8s.io", Version: "v1", Kind: "ClusterRoleList"}
}

// APILifecycleRemoved is an autogenerated function, returning the release in which the API is no longer served as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:removed" tags in types.go or  "k8s:prerelease-lifecycle-gen:deprecated" plus three minor.
func (in *ClusterRoleList) APILifecycleRemoved() (major, minor int) {
	return 1, 22
}

// APILifecycleIntroduced is an autogenerated function, returning the release in which the API struct was introduced as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:introduced" tags in types.go.
func (in *Role) APILifecycleIntroduced() (major, minor int) {
	return 1, 6
}

// APILifecycleDeprecated is an autogenerated function, returning the release in which the API struct was or will be deprecated as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:deprecated" tags in types.go or  "k8s:prerelease-lifecycle-gen:introduced" plus three minor.
func (in *Role) APILifecycleDeprecated() (major, minor int) {
	return 1, 17
}

// APILifecycleReplacement is an autogenerated function, returning the group, version, and kind that should be used instead of this deprecated type.
// It is controlled by "k8s:prerelease-lifecycle-gen:replacement=<group>,<version>,<kind>" tags in types.go.
func (in *Role) APILifecycleReplacement() schema.GroupVersionKind {
	return schema.GroupVersionKind{Group: "rbac.authorization.k8s.io", Version: "v1", Kind: "Role"}
}

// APILifecycleRemoved is an autogenerated function, returning the release in which the API is no longer served as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:removed" tags in types.go or  "k8s:prerelease-lifecycle-gen:deprecated" plus three minor.
func (in *Role) APILifecycleRemoved() (major, minor int) {
	return 1, 22
}

// APILifecycleIntroduced is an autogenerated function, returning the release in which the API struct was introduced as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:introduced" tags in types.go.
func (in *RoleBinding) APILifecycleIntroduced() (major, minor int) {
	return 1, 6
}

// APILifecycleDeprecated is an autogenerated function, returning the release in which the API struct was or will be deprecated as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:deprecated" tags in types.go or  "k8s:prerelease-lifecycle-gen:introduced" plus three minor.
func (in *RoleBinding) APILifecycleDeprecated() (major, minor int) {
	return 1, 17
}

// APILifecycleReplacement is an autogenerated function, returning the group, version, and kind that should be used instead of this deprecated type.
// It is controlled by "k8s:prerelease-lifecycle-gen:replacement=<group>,<version>,<kind>" tags in types.go.
func (in *RoleBinding) APILifecycleReplacement() schema.GroupVersionKind {
	return schema.GroupVersionKind{Group: "rbac.authorization.k8s.io", Version: "v1", Kind: "RoleBinding"}
}

// APILifecycleRemoved is an autogenerated function, returning the release in which the API is no longer served as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:removed" tags in types.go or  "k8s:prerelease-lifecycle-gen:deprecated" plus three minor.
func (in *RoleBinding) APILifecycleRemoved() (major, minor int) {
	return 1, 22
}

// APILifecycleIntroduced is an autogenerated function, returning the release in which the API struct was introduced as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:introduced" tags in types.go.
func (in *RoleBindingList) APILifecycleIntroduced() (major, minor int) {
	return 1, 6
}

// APILifecycleDeprecated is an autogenerated function, returning the release in which the API struct was or will be deprecated as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:deprecated" tags in types.go or  "k8s:prerelease-lifecycle-gen:introduced" plus three minor.
func (in *RoleBindingList) APILifecycleDeprecated() (major, minor int) {
	return 1, 17
}

// APILifecycleReplacement is an autogenerated function, returning the group, version, and kind that should be used instead of this deprecated type.
// It is controlled by "k8s:prerelease-lifecycle-gen:replacement=<group>,<version>,<kind>" tags in types.go.
func (in *RoleBindingList) APILifecycleReplacement() schema.GroupVersionKind {
	return schema.GroupVersionKind{Group: "rbac.authorization.k8s.io", Version: "v1", Kind: "RoleBindingList"}
}

// APILifecycleRemoved is an autogenerated function, returning the release in which the API is no longer served as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:removed" tags in types.go or  "k8s:prerelease-lifecycle-gen:deprecated" plus three minor.
func (in *RoleBindingList) APILifecycleRemoved() (major, minor int) {
	return 1, 22
}

// APILifecycleIntroduced is an autogenerated function, returning the release in which the API struct was introduced as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:introduced" tags in types.go.
func (in *RoleList) APILifecycleIntroduced() (major, minor int) {
	return 1, 6
}

// APILifecycleDeprecated is an autogenerated function, returning the release in which the API struct was or will be deprecated as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:deprecated" tags in types.go or  "k8s:prerelease-lifecycle-gen:introduced" plus three minor.
func (in *RoleList) APILifecycleDeprecated() (major, minor int) {
	return 1, 17
}

// APILifecycleReplacement is an autogenerated function, returning the group, version, and kind that should be used instead of this deprecated type.
// It is controlled by "k8s:prerelease-lifecycle-gen:replacement=<group>,<version>,<kind>" tags in types.go.
func (in *RoleList) APILifecycleReplacement() schema.GroupVersionKind {
	return schema.GroupVersionKind{Group: "rbac.authorization.k8s.io", Version: "v1", Kind: "RoleList"}
}

// APILifecycleRemoved is an autogenerated function, returning the release in which the API is no longer served as int versions of major and minor for comparison.
// It is controlled by "k8s:prerelease-lifecycle-gen:removed" tags in types.go or  "k8s:prerelease-lifecycle-gen:deprecated" plus three minor.
func (in *RoleList) APILifecycleRemoved() (major, minor int) {
	return 1, 22
}
