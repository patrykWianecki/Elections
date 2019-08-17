package com.app.utils;

import com.app.exceptions.MyException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class FileManager {

    @Value("${img.path}")
    private static String imgPath;

    private String generateFilename(MultipartFile multipartFile) {
        Optional.ofNullable(multipartFile).orElseThrow(() -> new MyException("Multipart file is null"));

        final String originalFilename = Optional.ofNullable(multipartFile.getOriginalFilename())
            .orElseThrow(() -> new MyException("Missing original filename in file " + multipartFile.getName()));
        final String[] arr = originalFilename.split("\\.");
        final String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));

        return filename + "." + arr[arr.length - 1];
    }

    public String addFile(MultipartFile multipartFile) {
        try {
            validateMultipartFile(multipartFile);
            final String filename = generateFilename(multipartFile);
            FileCopyUtils.copy(multipartFile.getBytes(), new File(createFileFullPath(filename)));

            return filename;
        } catch (Exception e) {
            throw new MyException("Unable to add new file");
        }
    }

    public boolean updateFile(MultipartFile multipartFile, String filename) {
        try {
            Optional.ofNullable(filename).orElseThrow(() -> new MyException("Filename is null"));
            validateMultipartFile(multipartFile);
            FileCopyUtils.copy(multipartFile.getBytes(), new File(createFileFullPath(filename)));

            return true;
        } catch (Exception e) {
            throw new MyException("Unable to update existing file");
        }
    }

    public boolean removeFile(String filename) {
        try {
            new File(createFileFullPath(filename)).delete();
            return true;
        } catch (Exception e) {
            throw new MyException("Unable to delete file");
        }
    }

    private static void validateMultipartFile(final MultipartFile multipartFile) throws IOException {
        Optional.ofNullable(multipartFile).orElseThrow(() -> new MyException("Multipart file is null"));
        if (multipartFile.getBytes().length == 0) {
            throw new MyException("Multipart file length is equal to 0");
        }
    }

    private static String createFileFullPath(final String filename) {
        return imgPath + filename;
    }
}

