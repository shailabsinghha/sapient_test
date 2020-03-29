package com.sapient.al.service.files;

import com.sapient.al.dto.ProcessingData;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public interface FileService {

    Object readFile(MultipartFile file) throws IOException;
    Object readFile(File file) throws IOException;

    Resource writeToFile(Set<ProcessingData> data) throws IOException;

}
