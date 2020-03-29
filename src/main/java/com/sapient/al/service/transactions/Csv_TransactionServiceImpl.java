package com.sapient.al.service.transactions;

import com.sapient.al.model.Transaction;
import com.sapient.al.repo.TransactionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service("transaction_service")
@Slf4j
public class Csv_TransactionServiceImpl implements TransactionService{

    @Autowired
    private TransactionRepo transactionRepo;

    @Override
    public void writeData(List<Transaction> transaction_data) {

        log.info("persist");
        log.info("batch size: {}", transaction_data.size());

        if(!transaction_data.isEmpty()) transactionRepo.saveAll(transaction_data);
    }

    @Override
    public List<Transaction> getAllData() {
        return transactionRepo.findAll();
    }
}
