package com.sapient.al.service.files;

import com.sapient.al.exceptions.FileTypeNotSupported;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FileServiceFactory {

    @Autowired
    @Qualifier("csv_file_service")
    private FileService csv_file_service;

    public FileService getInstance(String type) throws FileTypeNotSupported {

        if(type.equals("csv"))
            return csv_file_service;

        throw  new FileTypeNotSupported("FILE FORMAT NOT ACCEPTED");
    }

}
