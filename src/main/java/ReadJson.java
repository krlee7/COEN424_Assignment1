import Profiles.csvData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ReadJson {
    public static void returnJson(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(filePath);

        List<csvData> jsonList = mapper.readValue(file, new TypeReference<List<csvData>>() {});
        System.out.println(jsonList.size());
        for(int i=0; i<2; i++){
            System.out.println("Cpu: " + jsonList.get(i).getCPU());
            System.out.println("NetworkIn: " + jsonList.get(i).getNetworkIn());
            System.out.println("NetworkOut: " + jsonList.get(i).getNetworkOut());
            System.out.println("Memory: " + jsonList.get(i).getMemory());
        }

    }

    public static void main(String args[]) throws IOException {

        String currentDir = System.getProperty("user.dir");
        String newFile = currentDir + "\\src\\DVD_testing.json";

        returnJson(newFile);
    }
}
