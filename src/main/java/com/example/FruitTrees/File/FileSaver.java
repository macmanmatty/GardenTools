package com.example.FruitTrees.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileSaver {
   private static final ObjectMapper objectMapper = new ObjectMapper();
   private final static Logger logger= LoggerFactory.getLogger(FileSaver.class);

    public static  void saveFile(String path, String type, Object o)  {
        if(path==null|| path.isEmpty() || path.isBlank()){
            logger.info("file path is null");
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            if (type.equalsIgnoreCase("csv")) {
                saveToCsv(path, o);
            } else if (type.equalsIgnoreCase("XLS")) {
                saveToXls(path, o);
            } else {
                saveToJson(path, o);
            }
        }
        catch (IOException e){
            logger.info("file path is null");

        }
    }

    private  static void saveToJson(String path, Object o) throws IOException {
        objectMapper.writeValue(new File(path), o);
    }

   private  static void saveToCsv(String path, Object o) throws IOException {
        objectMapper.writeValue(new File(path), o);
    }
    private  static void saveToXls(String path, Object o) throws IOException {
        objectMapper.writeValue(new File(path), o);
    }

}
