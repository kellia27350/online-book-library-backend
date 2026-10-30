package com.library.online_book_library.repository;

import com.library.online_book_library.entity.Librarian;
import com.library.online_book_library.entity.Loan;
import com.library.online_book_library.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {

    // B2 – loans filtered by status, oldest first
    List<Loan> findByStatusOrderByLoanDateAsc(LoanStatus status);

    // B3 – loans within a date range, sorted by loanDate
    List<Loan> findByLoanDateBetweenOrderByLoanDateAsc(LocalDate start, LocalDate end);

    // D1 – check if a librarian already has a non-DECLINED loan on a given date
    boolean existsByLibrarianAndLoanDateAndStatusNot(Librarian librarian, LocalDate loanDate, LoanStatus status);

    // F1 – count loans grouped by status
    @Query("SELECT lo.status, COUNT(lo) FROM Loan lo GROUP BY lo.status")
    List<Object[]> groupByStatus();

    // F2 – bulk cancel loans for a librarian on a date (skip already RETURNED)
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Loan lo SET lo.status = com.library.online_book_library.entity.LoanStatus.DECLINED WHERE lo.librarian.id = :librarianId AND lo.loanDate = :date AND lo.status <> com.library.online_book_library.entity.LoanStatus.RETURNED")
    int cancelLoansForLibrarianOnDate(@Param("librarianId") UUID librarianId, @Param("date") LocalDate date);

    // dashboard – count loans by specific status
    long countByStatus(LoanStatus status);

    // dashboard – loans created today
    long countByLoanDate(LocalDate date);
}
