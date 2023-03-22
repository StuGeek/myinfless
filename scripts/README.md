
# Instructions
The following steps will reproduce the results in Figure 11 (system throuhgput comparison between INFless and its baseline).

## 1. Requirements Preparation

**<font color=Red>Note: The OS is preferably ubuntu 20.04. If not, problems may be encountered due to incorrect file formats.</font>**

First, you need to download Docker. Please refer to
https://docs.docker.com/engine/install/ubuntu/.

Second, you need to download Kind. Please refer to https://kind.sigs.k8s.io/docs/user/quick-start/.

Third, you need to open a kind cluster using the following commands.

```bash
$ cd ~/INFless-/scripts/
# copy the file to mount profiler later
$ cp -r ~/INFless-/profiler/ /home
# copy the file to mount clusterCapConfig-dev later
$ cp ~/INFless-/sourceCode/Go/src/github.com/openfaas/faas-netes/yaml/clusterCapConfig-dev.yml /home
$ kind create cluster --config mykind.yaml
Creating cluster "kind" ...
 ✓ Ensuring node image (kindest/node:v1.25.3) 🖼
 ✓ Preparing nodes 📦  
 ✓ Writing configuration 📜 
 ✓ Starting control-plane 🕹️ 
 ✓ Installing CNI 🔌 
 ✓ Installing StorageClass 💾 
Set kubectl context to "kind-kind"
You can now use your cluster with:

kubectl cluster-info --context kind-kind

Not sure what to do next? 😅  Check out https://kind.sigs.k8s.io/docs/user/quick-start/
```

Some other useful commands are given as follows.

```bash
$ kind get clusters
$ kind delete cluster
```

After that, you should turn into the directory of `INFless-` project and follow the subsequent instructions to reproduce the experimental results.
```bash
# Successfully login the private server and turn into INFless- workspace
$ cd ~/INFless-/ 
$ ls
configuration  LICENSE  profiler   scripts     workload
developer      models   README.md  sourceCode
```

## 2. Build and launch INFless- framework
INFless- is fully implemented within OpenFaaS, which is a FaaS platform runs on Kubernetes. To install INFless-, firstly, you should compile and build the docker images for each component. Using the following commands to compile codes for faasdev-cli, faas-gateway and faas-netes.

First, you should firstly switch to the root user.
```bash
$ sudo su
```

Compile and install INFless-.

```bash
$ cd ~/INFless-/sourceCode/Go/src/github.com/openfaas/
$ ls
faas  faas-cli  faas-idler  faas-netes
```

Compile faas-cli and move the executable to `/bin`.

```bash
$ cd ~/INFless-/sourceCode/Go/src/github.com/openfaas/faas-cli
$ make
$ cp faas-cli /bin/faasdev-cli
```

Compile gateway.

```bash
$ cd ~/INFless-/sourceCode/Go/src/github.com/openfaas/faas/gateway
$ make
$ kind load docker-image openfaas/gateway:latest --name kind
```

Compile faas-netes.
```bash
$ cd ~/INFless-/sourceCode/Go/src/github.com/openfaas/faas-netes
$ make
$ kind load docker-image openfaas/faas-netes:latest --name kind
```

Install INFless- on Kubernetes.
```bash
$ cd ~/INFless-/sourceCode/Go/src/github.com/openfaas/faas-netes
$ kubectl apply -f namespaces.yml
$ kubectl -n openfaasdev create secret generic basic-auth --from-literal=basic-auth-user=admin --from-literal=basic-auth-password=admin
$ kubectl apply -f ./yaml/inuse
```
>Notice: Need to modify the contents of this file 'gateway-dep.yml'; change the value of 'faas_loadgen_host' to the host ip of the workload service in this file

List the components of INFless-.
```bash
$ kubectl get pods -n openfaasdev 
root@ecs-5013:~/INFless-/sourceCode/Go/src/github.com/openfaas/faas-netes# kubectl get pods -n openfaasdev 
NAME                                               READY   STATUS    RESTARTS   AGE
basic-auth-plugindev-59495d4d5d-r5xlz              1/1     Running   0          7m29s
pod/cpuagentcontroller-deploy-0-6687bc6f4b-47j57   1/1     Running   0          7m29s
pod/cpuagentcontroller-deploy-1-75588ccd9b-9kg8x   1/1     Running   0          7m29s
gatewaydev-76dcc7f856-v68kc                        2/2     Running   0          7m29s
prometheusdev-5d988b56c5-fmpbp                     1/1     Running   0          7m29s
```

List the log of a specific pod such as gatewaydev-76dcc7f856-v68kc in the example below.
```bash
$ kubectl logs gatewaydev-76dcc7f856-v68kc -c faas-netesdev -n openfaasdev --since 0
```

p.s. Please wait until the above three pods are running to enter the next step (Maybe needs 1 minutes).

**Note that, for each re-compile and reinstall of INFless-, you must delete the old namespace firstly!!!**
```bash
$ kubectl delete ns openfaasdev-fn --force --grace-period=0
$ kubectl delete ns openfaasdev --force --grace-period=0
```

## 3. Deploy infererence functions
The inference model files are stored in directory of `~/INFless-/developer/servingFunctions/`
```bash
$ cd ~/INFless-/developer/servingFunctions/
$ faasdev-cli login -u admin -p admin
WARNING! Using --password is insecure, consider using: cat ~/faas_pass.txt | faas-cli login -u user --password-stdin
Calling the OpenFaaS server to validate the credentials...
WARNING! Communication is not secure, please consider using HTTPS. Letsencrypt.org offers free SSL/TLS certificates.
credentials saved for admin http://127.0.0.1:31212
```
p.s. If you meet any problems of IP connection in the above command, please try the following commands.

```bash
echo export OPENFAASDEV_URL=127.0.0.1:31212 >> ~/.bashrc
source ~/.bashrc
```
Download tensorflow/serving image
```bash
$ docker pull sdcbench/tfseving-infless:latest
$ docker tag sdcbench/tfseving-infless:latest tensorflow/serving:latest-gpu
```
Build & Deploy function
```bash
$ faasdev-cli build -f mobilenet.yml
$ kind load docker-image mobilenet:latest --name kind
$ faasdev-cli deploy -f mobilenet.yml
```

The build and deployment of Resnet-50 is similar as above.

The deployed inference functions can be listed as follows:
```bash
$ kubectl get all -n openfaasdev-fn
NAME                TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
service/mobilenet   ClusterIP   10.102.207.241   <none>        8080/TCP   2m5s
service/resnet-50   ClusterIP   10.97.239.55     <none>        8080/TCP   2m17s
service/ssd         ClusterIP   10.102.74.237    <none>        8080/TCP   2m11s
```
  
## 4. Start Workload Generator
Deploy load generator
```
Reference '~/INFless-/INFless-main/sourceCode/Java/LoadGen/README.md' file for deployment
```

Start the load generator using the following command,

```bash
$ cd ~/INFless-/workload/
# stop the workload 
$ jps -l |grep Load |awk '{print $1}' |xargs kill -9
# start the workload
$ sh start_load.sh 192.168.1.109 22222
```
> Notice1: The `start_load.sh` will run as a daemon and print some log. Please start a new terminal to run the commands in step 4.
> Notice2: If you run the program on the virtual machine or a computer with limited computing power, it is recommended that you use `start_ load_ light.sh` to replace the `start_load.sh` in the above command.

## 5. Collect the system log and Check result

**collect_result.sh can not work yet, because it requires you to manually extract logs from the container. However, you can see the generated latency information records in the results folder at this time.**

The following commands will collect INFless-'s runtime log and parse the results for system throughput comparison between `INFless-` and its baseline (`BATCH`). 

```bash
# parse results 
$ cd ~/INFless-/workload
$ sh collect_result.sh
prefixPath:~/INFless-/workload/
Baseline: BATCH
Total statistics QPS:52810
Scaling Efficiency: 0.498
Throughput Efficiency: 8.23954E-4
---------------------------
Baseline: INFless
Total statistics QPS:12068
Scaling Efficiency: 0.8135
Throughput Efficiency: 0.001874
```

The result shows that `INFless-` achieves >2x higher throughput than BATCH as in Figure 11 (0.00187 v.s. 0.00082).
