package Profiles;

public class DVD_Testing {
    private int CPU;
    private int networkIN;
    private int networkOut;
    private int memory;

    public DVD_Testing (int CPU, int networkIN, int networkOut, int memory) {
        this.CPU = CPU;
        this.networkIN = networkIN;
        this.networkOut = networkOut;
        this.memory = memory;
    }

    public int getCPU() {
        return CPU;
    }

    public int getNetworkIN() {
        return networkIN;
    }

    public int getNetworkOut() {
        return networkOut;
    }

    public int getMemory() {
        return memory;
    }



}
