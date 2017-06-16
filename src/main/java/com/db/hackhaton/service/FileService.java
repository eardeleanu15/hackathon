package com.db.hackhaton.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Service
public class FileService {

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    @Value( "${application.uploadPath}" )
    private String uploadDirectory;

    public boolean uploadFile(String name, MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();

            File dir = new File(uploadDirectory);
            if (!dir.exists()) {
                log.debug("Creating upload directory");
                dir.mkdirs();
            }

            // Create the file on server
            File serverFile = new File(dir.getAbsolutePath()
                + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(serverFile));
            stream.write(bytes);
            stream.close();

            log.debug("Success upload at location="
                + serverFile.getAbsolutePath());

            return true;
        } catch (Exception e) {
            log.debug("File upload failed");
            return false;
        }
    }

}
