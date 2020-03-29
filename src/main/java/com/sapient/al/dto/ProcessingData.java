package com.sapient.al.dto;

import com.sapient.al.model.Transaction;
import lombok.Data;

@Data
public
class ProcessingData{

    String clientId;
    String transactionType;
    String transactionDate;
    String priority;
    Float processingFee;


    public ProcessingData(Transaction transaction, Float processingFee) {
        this.clientId = transaction.getClientId();
        this.transactionType = transaction.getTransactionType();
        this.transactionDate = transaction.getTransactionDate();
        this.priority =  transaction.getPriorityFlag();
        this.processingFee = processingFee;
    }

    static public String getCsvHeader(){

        return   "Client Id, Transaction Type,  Transaction Date,  Priority , Processing Fees";

    }


}