package com.sapient.al.service.files;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.sapient.al.dto.ProcessingData;
import com.sapient.al.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("csv_file_service")
@Slf4j
public class CsvFileServiceImpl implements FileService {

    static HeaderColumnNameTranslateMappingStrategy<Transaction> strategy =
            new HeaderColumnNameTranslateMappingStrategy<>();

    static {

        Map<String, String> mapping = new HashMap<>();
        mapping.put("External Transaction Id ", "ExternalTransactionId");
        mapping.put(" Client Id ", "ClientId");
        mapping.put(" Security Id ", "SecurityId");
        mapping.put(" Transaction Type ", "TransactionType");
        mapping.put(" Transaction Date ", "TransactionDate");
        mapping.put(" Market Value ", "MarketValue");
        mapping.put(" Priority Flag", "PriorityFlag");

        strategy.setType(Transaction.class);
        strategy.setColumnMapping(mapping);
    }

    @Override
    public List<Transaction> readFile(MultipartFile file) throws IOException {

        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());
        file.transferTo(convFile);
        return readFile(convFile);

    }

    @Override
    public List<Transaction> readFile(File file) throws IOException {

        CSVReader csvReader =  new CSVReader(new FileReader(file));
        return new CsvToBean().parse(strategy, csvReader);

    }


    @Override
    public Resource writeToFile(Set<ProcessingData> data){

        StringBuilder csvWriter = new StringBuilder();
        csvWriter.append(ProcessingData.getCsvHeader());
        csvWriter.append("\n");

        for (ProcessingData rowData :  data) {
            csvWriter.append(rowData.getClientId());
            csvWriter.append(",");

            csvWriter.append(rowData.getTransactionType());
            csvWriter.append(",");

            csvWriter.append(rowData.getTransactionDate());
            csvWriter.append(",");

            csvWriter.append(rowData.getPriority());
            csvWriter.append(",");

            csvWriter.append(rowData.getProcessingFee());
            csvWriter.append("\n");
        }

        return new ByteArrayResource(csvWriter.toString().getBytes());
    }


}
