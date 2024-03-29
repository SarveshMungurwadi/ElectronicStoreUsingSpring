package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        String originalFileName=file.getOriginalFilename();
        logger.info("Filename : {}",originalFileName);
        String filename= UUID.randomUUID().toString();
        String extension=originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileNameWithExtension=filename+extension;
        String fullPathWithFileName= path+fileNameWithExtension;
//        logger.info("Full Path with FileName: {}", fullPathWithFileName);

        logger.info("Working Directory: {}", System.getProperty("user.dir"));

        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")){
            File folder=new File(path);
            if(!folder.exists()){
                //create folder
                folder.mkdirs();
            }
            //upload file
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }
        else{
            throw new BadApiRequest("File with this "+extension+"not allowed");
        }

    }

    @Override
    public InputStream getResources(String path, String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }
}
