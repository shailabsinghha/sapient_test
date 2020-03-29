package com.sapient.al.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvDate;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {

    @Id
    private String ExternalTransactionId;

     @Column
    private String ClientId;

     @Column
    private String SecurityId;

    @Column
    private String TransactionType;

    @Column @CsvDate(value = "mm/dd/yyyy")
    private String TransactionDate;

    @Column
    private Float MarketValue;

    @Column
    private String PriorityFlag;


}
