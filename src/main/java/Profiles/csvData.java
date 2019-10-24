package Profiles;

public class csvData {

        private double cpu;
        private double networkIn;
        private double networkOut;
        private double memory;

        public csvData(){};

        public csvData (double cpu, double networkIn, double networkOut, double memory) {
            this.cpu = cpu;
            this.networkIn = networkIn;
            this.networkOut = networkOut;
            this.memory = memory;
        }

        public double getCPU() {
            return cpu;
        }

        public double getNetworkIn() {
            return networkIn;
        }

        public double getNetworkOut() {
            return networkOut;
        }

        public double getMemory() {
            return memory;
        }

}
