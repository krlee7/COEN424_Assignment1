import Profiles.csvData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ReadJson {
    public static void returnJson(String filePath, String benchmarkType, String testType, String metric, int batchUnit, int batchID, int batchSize) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath + "\\" + benchmarkType + "_" + testType + ".json");



        List<csvData> jsonList = mapper.readValue(file, new TypeReference<List<csvData>>() {});
        int totalSampleSize = jsonList.size();
        int sampleStart = batchID*batchUnit;
        int sampleEnd = (batchID + batchSize)*batchUnit;

        System.out.println("All samples = " + totalSampleSize);
        System.out.println("Number of partitions = " + (totalSampleSize/batchUnit));
        System.out.println("Start at sample number " + sampleStart);
        System.out.println("End at sample number " + sampleEnd);

        if(sampleEnd/batchUnit <= totalSampleSize/batchUnit) {
            for (int i = sampleStart; i < sampleEnd; i++) {
                if(metric.matches("(?i)cpu"))
                    System.out.println("Cpu: " + jsonList.get(i).getCPU());
                else if(metric.matches("(?i)networkin"))
                    System.out.println("NetworkIn: " + jsonList.get(i).getNetworkIn());
                else if(metric.matches("(?i)networkout"))
                    System.out.println("NetworkOut: " + jsonList.get(i).getNetworkOut());
                else if(metric.matches("(?i)memory"))
                    System.out.println("Memory: " + jsonList.get(i).getMemory());
                else
                    System.out.println("Not a proper metric");
            }
        }
        //Get the samples in the last partition that may not have the same number of batch units
        else if(sampleEnd/batchUnit <= (totalSampleSize/batchUnit)+1) {
            sampleEnd = totalSampleSize;
            System.out.println("Last sample is " + sampleEnd);
            for(int i = sampleStart; i <= sampleEnd; i++){
                if(metric.matches("(?i)cpu"))
                    System.out.println("Cpu: " + jsonList.get(i).getCPU());
                else if(metric.matches("(?i)networkin"))
                    System.out.println("NetworkIn: " + jsonList.get(i).getNetworkIn());
                else if(metric.matches("(?i)networkout"))
                    System.out.println("NetworkOut: " + jsonList.get(i).getNetworkOut());
                else if(metric.matches("(?i)memory"))
                    System.out.println("Memory: " + jsonList.get(i).getMemory());
                else
                    System.out.println("Not a proper metric");
            }
        }
        else{
            System.out.println("OUT OF BOUNDS");
        }
    }

    public static void main(String args[]) throws IOException {

        String currentDir = System.getProperty("user.dir");
        String newFile = currentDir + "\\src";

        returnJson(newFile, "DVD", "testing","cpu", 100,126,1);
    }
}
