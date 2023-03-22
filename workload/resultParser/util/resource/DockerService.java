// 
// Decompiled by Procyon v0.5.36
// 

package util.resource;

public class DockerService
{
    private static DockerService service;
    
    static {
        DockerService.service = null;
    }
    
    private DockerService() {
    }
    
    public static synchronized DockerService getInstance() {
        if (DockerService.service == null) {
            DockerService.service = new DockerService();
        }
        return DockerService.service;
    }
    
    public String genRunCommand(final String modelName, final int batchSize, final int GpuQuota, final int socketId, final int CpuQuota) {
        String GpuDeviceId = "";
        if (GpuQuota == 0) {
            GpuDeviceId = "-1";
        }
        else {
            GpuDeviceId = Integer.toString(socketId);
        }
        final String cpuStr = this.genCpuStr(socketId, CpuQuota);
        final String dockerCommand = "docker run -itd --name=" + modelName + " --ipc=host " + "-p 9501:8501 --cpuset-cpus=" + cpuStr + " -v /home/tank/yanan/models/" + modelName + ":/models/" + modelName + " " + "-v /home/tank/yanan/models/batching_parameters_" + batchSize + ".txt:/models/batching_parameters_" + batchSize + ".txt " + "-e CUDA_VISIBLE_DEVICES=" + GpuDeviceId + " " + "-e MODEL_NAME=" + modelName + " " + "-e CUDA_MPS_ACTIVE_THREAD_PERCENTAGE=" + GpuQuota + " " + "tensorflow/serving:latest-gpu " + "--enable_batching=true " + "--batching_parameters_file=/models/batching_parameters_" + batchSize + ".txt " + "--per_process_gpu_memory_fraction=0.3";
        System.out.println(dockerCommand);
        return dockerCommand;
    }
    
    private String genCpuStr(final int socketId, final int CpuQuota) {
        final StringBuilder cpuStrBuilder = new StringBuilder();
        final int length = CpuQuota / 2;
        final int offset = 10 * socketId;
        for (int i = 0; i < length; ++i) {
            cpuStrBuilder.append(Integer.toString(i + offset)).append(",").append(Integer.toString(i + offset + 40)).append(",");
        }
        String cpuStr = cpuStrBuilder.toString();
        cpuStr = cpuStr.toString().substring(0, cpuStr.length() - 1);
        cpuStrBuilder.setLength(0);
        return cpuStr;
    }
    
    public String genStopCommand(final String modelName, final int batchSize, final int GpuQuota, final int socketId, final int CpuQuota) {
        final String dockerCommand = "docker stop " + modelName;
        System.out.println(dockerCommand);
        return dockerCommand;
    }
    
    public String genRemoveCommand(final String modelName) {
        final String dockerCommand = "docker rm " + modelName;
        System.out.println(dockerCommand);
        return dockerCommand;
    }
    
    public String genUpdateCommand(final String modelName, final int socketId, final int CpuQuota) {
        final String cpuStr = this.genCpuStr(socketId, CpuQuota);
        final String dockerCommand = "docker update " + modelName + " --cpuset-cpus=" + cpuStr;
        System.out.println(dockerCommand);
        return dockerCommand;
    }
    
    public static void main(final String[] args) {
    }
}
