package com.sapient.al.service.transactions;

import com.sapient.al.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface TransactionService {

    void writeData(List<Transaction> transaction_data);
    List<Transaction> getAllData();

}
