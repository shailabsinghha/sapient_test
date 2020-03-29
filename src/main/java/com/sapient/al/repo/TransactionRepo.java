package com.sapient.al.repo;

import com.sapient.al.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
}
