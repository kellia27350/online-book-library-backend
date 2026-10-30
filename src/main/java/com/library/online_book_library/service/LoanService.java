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

    // before saving a loan I check two things:
    // 1. the date cannot be in the past
    // 2. the librarian should not already have a non-declined loan on that same day
    // whatever status is sent in the body gets ignored, loan always starts as REQUESTED
    public Object save(Loan loan) {
        if (loan.getLoanDate().isBefore(LocalDate.now())) {
            return "Loan date cannot be in the past";
        }

        boolean librarianBusy = loanRepository.existsByLibrarianAndLoanDateAndStatusNot(
                loan.getLibrarian(), loan.getLoanDate(), LoanStatus.DECLINED);
        if (librarianBusy) {
            return "Librarian already issues a loan on that date";
        }

        loan.setStatus(LoanStatus.REQUESTED);
        return loanRepository.save(loan);
    }

    public List<Loan> getAll() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getById(UUID id) {
        return loanRepository.findById(id);
    }

    // regular update, only changes date, title, reader and librarian
    // the status field is intentionally left out here, it only moves via changeStatus
    public Optional<Loan> update(UUID id, Loan updated) {
        return loanRepository.findById(id).map(existing -> {
            existing.setLoanDate(updated.getLoanDate());
            existing.setBookTitle(updated.getBookTitle());
            existing.setReader(updated.getReader());
            existing.setLibrarian(updated.getLibrarian());
            return loanRepository.save(existing);
        });
    }

    // a loan with fines attached cannot be deleted
    // the librarian must clear the fines first before removing the loan
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

    // returns loans filtered by status sorted from oldest to newest
    public List<Loan> getByStatus(LoanStatus status) {
        return loanRepository.findByStatusOrderByLoanDateAsc(status);
    }

    // returns loans within a date range, both start and end included
    public List<Loan> getBetween(LocalDate start, LocalDate end) {
        return loanRepository.findByLoanDateBetweenOrderByLoanDateAsc(start, end);
    }

    // status can only move forward along a fixed path:
    // REQUESTED -> ISSUED or DECLINED
    // ISSUED -> RETURNED or DECLINED
    // RETURNED and DECLINED are final, nothing can move out of them
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

    // groups loans by status and counts each group
    public List<Object[]> getStatsByStatus() {
        return loanRepository.groupByStatus();
    }

    // sets all loans of a librarian on a given day to DECLINED in one query
    // loans that are already RETURNED are skipped
    @Transactional
    public String cancelDay(UUID librarianId, LocalDate date) {
        int count = loanRepository.cancelLoansForLibrarianOnDate(librarianId, date);
        return count + " loans cancelled";
    }

    // returns a page of loans, supports sorting and pagination from the controller
    public Page<Loan> getPage(Pageable pageable) {
        return loanRepository.findAll(pageable);
    }
}
