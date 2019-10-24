import Profiles.csvData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ReadJson {
    public static void returnJson(String filePath, int batchUnit, int batchID, int batchSize) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);

        List<csvData> jsonList = mapper.readValue(file, new TypeReference<List<csvData>>() {});
        int totalSampleSize = jsonList.size();
        int sampleStart = batchID*batchUnit;
        int sampleEnd = (batchID + batchSize)*batchUnit;

        System.out.println("All samples = " + totalSampleSize);
        System.out.println("Number of partitions = " + (totalSampleSize/batchUnit));
        System.out.println("Start at sample number " + sampleStart);
        System.out.println("Start at sample number " + sampleEnd);

        if(sampleEnd/batchUnit <= totalSampleSize/batchUnit) {
            for (int i = sampleStart; i < sampleEnd; i++) {
//                System.out.println("Cpu: " + jsonList.get(i).getCPU());
//                System.out.println("NetworkIn: " + jsonList.get(i).getNetworkIn());
//                System.out.println("NetworkOut: " + jsonList.get(i).getNetworkOut());
//                System.out.println("Memory: " + jsonList.get(i).getMemory());
            }
        }
        //Get the samples in the last partition that may not have the same number of batch units
        else if(sampleEnd/batchUnit <= (totalSampleSize/batchUnit)+1) {
            sampleEnd = totalSampleSize;
            System.out.println("Last sample is " + sampleEnd);
            for(int i = sampleStart; i <= sampleEnd; i++){
                //Iterate through samples
            }
        }
        else{
            System.out.println("OUT OF BOUNDS");
        }
    }

    public static void main(String args[]) throws IOException {

        String currentDir = System.getProperty("user.dir");
        String newFile = currentDir + "\\src\\DVD_testing.json";

        returnJson(newFile, 100,127,1);
    }
}
