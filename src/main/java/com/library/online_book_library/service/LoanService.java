package com.library.online_book_library.service;

import com.library.online_book_library.entity.Loan;
import com.library.online_book_library.entity.LoanStatus;
import com.library.online_book_library.repository.FineRepository;
import com.library.online_book_library.repository.LoanRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final FineRepository fineRepository;

    public LoanService(LoanRepository loanRepository, FineRepository fineRepository) {
        this.loanRepository = loanRepository;
        this.fineRepository = fineRepository;
    }

    // D1 – safe save with date and duplicate checks
    public Object save(Loan loan) {
        if (loan.getLoanDate().isBefore(LocalDate.now())) {
            return "Loan date cannot be in the past";
        }

        boolean alreadyBusy = loanRepository.existsByLibrarianAndLoanDateAndStatusNot(
                loan.getLibrarian(), loan.getLoanDate(), LoanStatus.DECLINED);
        if (alreadyBusy) {
            return "Librarian already issues a loan on that date";
        }

        // new loan always starts as REQUESTED
        loan.setStatus(LoanStatus.REQUESTED);
        return loanRepository.save(loan);
    }

    public List<Loan> getAll() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getById(UUID id) {
        return loanRepository.findById(id);
    }

    // general update – does NOT change status (status only moves via changeStatus)
    public Optional<Loan> update(UUID id, Loan updated) {
        return loanRepository.findById(id).map(existing -> {
            existing.setLoanDate(updated.getLoanDate());
            existing.setBookTitle(updated.getBookTitle());
            existing.setReader(updated.getReader());
            existing.setLibrarian(updated.getLibrarian());
            return loanRepository.save(existing);
        });
    }

    // E3 – block delete when fines exist
    public String delete(UUID id) {
        Optional<Loan> opt = loanRepository.findById(id);
        if (opt.isEmpty()) return "The loan with that id does not exist";

        Loan loan = opt.get();
        if (fineRepository.existsByLoan(loan)) {
            return "That loan has fines, delete them first";
        }

        loanRepository.deleteById(id);
        return "Loan deleted successfully";
    }

    // B2 – loans by status, earliest first
    public List<Loan> getByStatus(LoanStatus status) {
        return loanRepository.findByStatusOrderByLoanDateAsc(status);
    }

    // B3 – loans in a date range
    public List<Loan> getBetween(LocalDate start, LocalDate end) {
        return loanRepository.findByLoanDateBetweenOrderByLoanDateAsc(start, end);
    }

    // D2 – status flow enforcement
    public String changeStatus(UUID id, LoanStatus newStatus) {
        Optional<Loan> opt = loanRepository.findById(id);
        if (opt.isEmpty()) return "The loan with that id does not exist";

        Loan loan = opt.get();
        LoanStatus current = loan.getStatus();

        boolean allowed = (current == LoanStatus.REQUESTED && (newStatus == LoanStatus.ISSUED || newStatus == LoanStatus.DECLINED))
                || (current == LoanStatus.ISSUED && (newStatus == LoanStatus.RETURNED || newStatus == LoanStatus.DECLINED));

        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }

        loan.setStatus(newStatus);
        loanRepository.save(loan);
        return "Loan status updated successfully";
    }

    // F1 – count per status
    public List<Object[]> getStatsByStatus() {
        return loanRepository.groupByStatus();
    }

    // F2 – bulk cancel a librarian's loans on a date
    @Transactional
    public String cancelDay(UUID librarianId, LocalDate date) {
        int count = loanRepository.cancelLoansForLibrarianOnDate(librarianId, date);
        return count + " loans cancelled";
    }

    // Bonus – paginated loans
    public Page<Loan> getPage(Pageable pageable) {
        return loanRepository.findAll(pageable);
    }
}
