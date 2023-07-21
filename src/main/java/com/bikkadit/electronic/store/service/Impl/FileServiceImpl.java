package com.bikkadit.electronic.store.service.Impl;

import com.bikkadit.electronic.store.exception.BadApiRequestException;
import com.bikkadit.electronic.store.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl  implements FileService {


    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFilename = file.getOriginalFilename();
        logger.info("Filename : {}",originalFilename);
        String filename= UUID.randomUUID().toString();
        String extention=originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtention=filename+extention;
        String fullPathWithFileName=path+ fileNameWithExtention;
        logger.info("full image path: {}",fullPathWithFileName );
        if(extention.equalsIgnoreCase(".png") || extention.equalsIgnoreCase(".jpg")|| extention.equalsIgnoreCase(".jpeg")){

            // file save

         logger.info("file extention is {}" , extention);
         File folder =new File(path);
         if ( !folder.exists()) {

             // create the folder
             folder.mkdirs();
         }
            // upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));

             return fileNameWithExtention;


        }else{

            throw new BadApiRequestException("File With this " +extention+ "not allowed !!");
        }


    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullPath=path+File.separator+name;
        InputStream inputStream = new FileInputStream(fullPath);


        return inputStream;
    }
}
