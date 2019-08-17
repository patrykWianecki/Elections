package com.app.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.app.exceptions.MyException;

@Component
public class FileManager {

    @Value("${img.path}")
    private String imgPath;

    private String generateFilename(MultipartFile multipartFile) {
        final String originalFilename = multipartFile.getOriginalFilename();
        final String[] arr = originalFilename.split("\\.");
        final String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
        return filename + "." + arr[arr.length - 1];
    }

    public String addFile(MultipartFile multipartFile) {
        try {
            if (multipartFile == null || multipartFile.getBytes().length == 0) {
                throw new IllegalArgumentException("FILE IS NOT CORRECT");
            }
            final String filename = generateFilename(multipartFile);
            final String fullPath = imgPath + filename;
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fullPath));
            return filename;
        } catch (Exception e) {
            throw new MyException("ADD FILE EXCEPTION");
        }
    }

    public boolean updateFile(MultipartFile multipartFile, String filename) {
        try {
            if (multipartFile == null || multipartFile.getBytes().length == 0) {
                return true;
            }
            final String fullPath = imgPath + filename;
            FileCopyUtils.copy(multipartFile.getBytes(), new File(fullPath));
            return true;
        } catch (Exception e) {
            throw new MyException("UPDATE FILE EXCEPTION");
        }
    }

    public boolean removeFile(String filename) {
        try {
            final String fullPath = imgPath + filename;
            new File(fullPath).delete();
            return true;
        } catch (Exception e) {
            throw new MyException("DELETE FILE EXCEPTION");
        }
    }
}

