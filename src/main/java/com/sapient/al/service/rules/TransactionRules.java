package com.sapient.al.service.rules;

import com.sapient.al.dto.ProcessingData;
import com.sapient.al.model.Transaction;

import java.util.List;
import java.util.Set;

public interface TransactionRules {
    Set<ProcessingData> getProcessingFees(List<Transaction> transactionList);
}
