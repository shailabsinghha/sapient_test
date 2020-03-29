package com.sapient.al.controllers;

import com.sapient.al.dto.ProcessingData;
import com.sapient.al.exceptions.FileTypeNotSupported;
import com.sapient.al.model.Transaction;
import com.sapient.al.service.rules.TransactionRulesImpl;
import com.sapient.al.service.transactions.TransactionService;
import com.sapient.al.service.files.FileService;
import com.sapient.al.service.files.FileServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@RestController
@RequestMapping("/v1/transaction")
@Slf4j
public class TransactionController {


    @Autowired
    @Qualifier("transaction_service")
    private TransactionService transactionService;

    @Autowired
    private TransactionRulesImpl intradayImpl;

    @Autowired
    private FileServiceFactory fileServiceFactory;

    @GetMapping
    public ResponseEntity<Resource> getAllTransaction(String type) throws FileTypeNotSupported, IOException {

        /************************************************************
         * 1. get the instance of the type of file required
         * 2. get all data from the db
         * 3. format data using the rules
         * 4. write and return the file
         *************************************************************/

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        FileService fileService = fileServiceFactory.getInstance(type);

        List<Transaction> data = transactionService.getAllData();
        Set<ProcessingData> processingFees = intradayImpl.getProcessingFees(data);

        return ResponseEntity.ok()
                .headers(header)
//                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(fileService.writeToFile(processingFees));

        //todo: processing the data also caching should be done for the current processing


    }

    @PutMapping
    public List<Transaction> putTransactions(@RequestParam("file") MultipartFile file)
            throws FileTypeNotSupported, IOException {

        // validate read then write field to db
        String ext;
        try {
            ext =Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        }catch (ArrayIndexOutOfBoundsException e){
            throw new FileTypeNotSupported("File Extension not supported");
        }
        FileService fileService = fileServiceFactory
                .getInstance(ext);
        List<Transaction> transactions = (List<Transaction>) fileService.readFile(file);
        log.info("data : {}", transactions);
        transactionService.writeData(transactions);
        return transactions;
    }

}
