package com.sapient.al.service.files;

import com.sapient.al.exceptions.FileTypeNotSupported;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FileServiceFactory {

    private final FileService csv_file_service;

    public FileServiceFactory(@Qualifier("csv_file_service") FileService csv_file_service) {
        this.csv_file_service = csv_file_service;
    }

    public FileService getInstance(String type) throws FileTypeNotSupported {

        if(type.equals("csv")) return csv_file_service;

        throw  new FileTypeNotSupported("FILE FORMAT NOT ACCEPTED");
    }

}
