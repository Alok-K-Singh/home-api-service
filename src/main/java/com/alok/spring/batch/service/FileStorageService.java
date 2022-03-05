package com.alok.spring.batch.service;

import com.alok.spring.batch.exception.FileStorageException;
import com.alok.spring.batch.utils.UploadType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    @Value("${dir.path.kotak_account.imported}")
    private String kotakImportedLocation;

    @Value("${dir.path.expense}")
    private String expenseDirLocation;

    private Path getStoragePath(UploadType uploadType) {
        if (uploadType.equals(UploadType.KotakExportedStatement))
            return Paths.get(kotakImportedLocation).toAbsolutePath().normalize();

        if (uploadType.equals(UploadType.ExpenseGoogleSheet))
            return Paths.get(expenseDirLocation).toAbsolutePath().normalize();

        throw new RuntimeException("Invalid Upload Type");
    }

    public String storeFile(MultipartFile file, UploadType uploadType) {
        // Normalize file name
        //String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Hard coding so that any file name uplaod will replace the same file
        String fileName = StringUtils.cleanPath("Expense Sheet - Form Responses 1.csv");

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path storageLocationPath = getStoragePath(uploadType);

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = storageLocationPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
