package Profiles;

public class csvData {

        private double CPU;
        private double networkIn;
        private double networkOut;
        private double memory;

        public csvData (double CPU, double networkIn, double networkOut, double memory) {
            this.CPU = CPU;
            this.networkIn = networkIn;
            this.networkOut = networkOut;
            this.memory = memory;
        }

        public double getCPU() {
            return CPU;
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
