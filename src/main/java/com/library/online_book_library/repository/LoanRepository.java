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

    List<Loan> findByStatusOrderByLoanDateAsc(LoanStatus status);

    List<Loan> findByLoanDateBetweenOrderByLoanDateAsc(LocalDate start, LocalDate end);

    boolean existsByLibrarianAndLoanDateAndStatusNot(Librarian librarian, LocalDate loanDate, LoanStatus status);

    @Query("SELECT lo.status, COUNT(lo) FROM Loan lo GROUP BY lo.status")
    List<Object[]> groupByStatus();

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Loan lo SET lo.status = com.library.online_book_library.entity.LoanStatus.DECLINED WHERE lo.librarian.id = :librarianId AND lo.loanDate = :date AND lo.status <> com.library.online_book_library.entity.LoanStatus.RETURNED")
    int cancelLoansForLibrarianOnDate(@Param("librarianId") UUID librarianId, @Param("date") LocalDate date);

    long countByStatus(LoanStatus status);

    long countByLoanDate(LocalDate date);
}
