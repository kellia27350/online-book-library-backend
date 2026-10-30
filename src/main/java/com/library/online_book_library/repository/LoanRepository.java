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

    // get loans by status sorted oldest first
    List<Loan> findByStatusOrderByLoanDateAsc(LoanStatus status);

    // get loans that fall between two dates, sorted by date
    List<Loan> findByLoanDateBetweenOrderByLoanDateAsc(LocalDate start, LocalDate end);

    // check if a librarian already has a loan on a date that is not declined
    boolean existsByLibrarianAndLoanDateAndStatusNot(Librarian librarian, LocalDate loanDate, LoanStatus status);

    // count how many loans exist per status
    @Query("SELECT lo.status, COUNT(lo) FROM Loan lo GROUP BY lo.status")
    List<Object[]> groupByStatus();

    // set all loans of a librarian on a specific date to DECLINED
    // skips loans that are already RETURNED
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Loan lo SET lo.status = com.library.online_book_library.entity.LoanStatus.DECLINED WHERE lo.librarian.id = :librarianId AND lo.loanDate = :date AND lo.status <> com.library.online_book_library.entity.LoanStatus.RETURNED")
    int cancelLoansForLibrarianOnDate(@Param("librarianId") UUID librarianId, @Param("date") LocalDate date);

    // used by the dashboard to count loans with a specific status
    long countByStatus(LoanStatus status);

    // used by the dashboard to count how many loans were created today
    long countByLoanDate(LocalDate date);
}
