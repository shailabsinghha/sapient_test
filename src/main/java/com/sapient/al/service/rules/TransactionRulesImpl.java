package com.sapient.al.service.rules;

import com.sapient.al.dto.ProcessingData;
import com.sapient.al.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionRulesImpl implements TransactionRules {

    /***************************************************************************
     * Intra day transactions
     * Make the date as the Key with all the transactions inside it a as a Key
     * Iterate through the map and find the processing fees
     **********************************************************************************/

    @Value("${intraDayFees}")
    private Float intraDayFees=10.0f;

    @Value("${normalPrioritySellAndWithdrawNormalTransactionsFees}")
    private Float normalPrioritySellAndWithdrawNormalTransactionsFees=100.0f;

    @Value("${highPriorityNormalTransactionsFees}")
    private Float highPriorityNormalTransactionsFees=500.0f;

    @Value("${normalPriorityBuyAndSellNormalTransactionsFees}")
    private Float normalPriorityBuyAndSellNormalTransactionsFees=50.0f;

    @Override
    public  Set<ProcessingData>  getProcessingFees(List<Transaction> transactionList){

        Map<String, List<Transaction>> formattedTransactionDataList= new HashMap<>();
        transactionList.forEach(transaction -> {

            String key = transaction.getTransactionDate() +
                    transaction.getClientId() +
                     transaction.getSecurityId();

            if(formattedTransactionDataList.containsKey(key)){

                if(formattedTransactionDataList.get(key).isEmpty()){
                    List<Transaction> set= new ArrayList<>();
                    set.add(transaction);
                    formattedTransactionDataList.put(key, set);
                }
                else{
                    List<Transaction> set= formattedTransactionDataList.get(key);
                    set.add(transaction);
                    formattedTransactionDataList.put(key, set);
                }
            }
            else{
                List<Transaction> set= new ArrayList<>();
                set.add(transaction);
                formattedTransactionDataList.put(key, set);
            }

        });
        Set<ProcessingData> processingDataSet = new HashSet<>();
        for (Map.Entry<String,List<Transaction>> entry : formattedTransactionDataList.entrySet()){


            Set<Transaction> intraDayTransactions = new HashSet<>();
            Set<Transaction> highPriorityNormalTransactions = new HashSet<>();
            Set<Transaction> normalPrioritySellAndWithdrawNormalTransactions = new HashSet<>();
            Set<Transaction> normalPriorityBuyAndSellNormalTransactions = new HashSet<>();

            boolean intraDayFlag = true;

            if(entry.getValue().size()==2){ // could be an intraday transaction

                intraDayFlag=false;

                Transaction t1= entry.getValue().get(0);
                Transaction t2= entry.getValue().get(1);

                if(t1.getTransactionType().trim().equalsIgnoreCase("buy") &&
                    t2.getTransactionType().trim().equalsIgnoreCase("sell")){
                    intraDayTransactions.add(t1);
                    intraDayTransactions.add(t2);
                    log.info("intra day transaction {} {}", t1, t2);

                }
                else
                    intraDayFlag=true;
            }
            if(intraDayFlag){

                    entry.getValue()
                            .forEach(transaction -> {
                                if (
                                        transaction.getPriorityFlag().trim().equalsIgnoreCase("Y")
                                )
                                    highPriorityNormalTransactions.add(transaction);

                                else if (
                                        transaction.getPriorityFlag().trim().equalsIgnoreCase("N") &&
                                                (
                                                        (transaction.getTransactionType().trim().equalsIgnoreCase("buy") ||
                                                                transaction.getTransactionType().trim().equalsIgnoreCase("sell"))
                                                )
                                )
                                    normalPriorityBuyAndSellNormalTransactions.add(transaction);

                                else if (
                                        transaction.getPriorityFlag().trim().equalsIgnoreCase("N") &&
                                                (
                                                        (transaction.getTransactionType().trim().equalsIgnoreCase("withdraw") ||
                                                                transaction.getTransactionType().trim().equalsIgnoreCase("deposit"))
                                                )
                                )
                                    normalPrioritySellAndWithdrawNormalTransactions.add(transaction);

                            });

            }
            processingDataSet.addAll(intraDayTransactions.parallelStream()
                   .map(transaction -> new ProcessingData(transaction, intraDayFees))
                    .collect(Collectors.toSet()));
            processingDataSet.addAll(highPriorityNormalTransactions.parallelStream()
                    .map(transaction -> new ProcessingData(transaction, highPriorityNormalTransactionsFees))
                    .collect(Collectors.toSet()));

            processingDataSet.addAll(normalPriorityBuyAndSellNormalTransactions.parallelStream()
                    .map(transaction -> new ProcessingData(transaction, normalPriorityBuyAndSellNormalTransactionsFees))
                    .collect(Collectors.toSet()));
            processingDataSet.addAll(normalPrioritySellAndWithdrawNormalTransactions.parallelStream()
                    .map(transaction -> new ProcessingData(transaction, normalPrioritySellAndWithdrawNormalTransactionsFees))
                    .collect(Collectors.toSet()));

        }
        return processingDataSet;

    }



}
