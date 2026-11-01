package com.library.online_book_library.repository;

import com.library.online_book_library.entity.Fine;
import com.library.online_book_library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface FineRepository extends JpaRepository<Fine, UUID> {

    boolean existsByLoan(Loan loan);

    @Query("SELECT f FROM Fine f WHERE f.loan.reader.cardNumber = :cardNumber ORDER BY f.chargedDate DESC")
    List<Fine> findByReaderCardNumber(@Param("cardNumber") String cardNumber);
}
