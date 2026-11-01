package com.library.online_book_library.service;

import com.library.online_book_library.entity.Fine;
import com.library.online_book_library.entity.Loan;
import com.library.online_book_library.entity.LoanStatus;
import com.library.online_book_library.repository.FineRepository;
import com.library.online_book_library.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FineService {

    private final FineRepository fineRepository;
    private final LoanRepository loanRepository;

    public FineService(FineRepository fineRepository, LoanRepository loanRepository) {
        this.fineRepository = fineRepository;
        this.loanRepository = loanRepository;
    }

    public Object save(Fine fine, UUID loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) return "The loan with that id does not exist";

        Loan loan = loanOpt.get();
        if (loan.getStatus() != LoanStatus.RETURNED) {
            return "Fines are only for RETURNED loans";
        }

        fine.setChargedDate(LocalDate.now());
        fine.setLoan(loan);
        return fineRepository.save(fine);
    }

    public List<Fine> getAll() {
        return fineRepository.findAll();
    }

    public Optional<Fine> getById(UUID id) {
        return fineRepository.findById(id);
    }

    public List<Fine> getByReaderCardNumber(String cardNumber) {
        return fineRepository.findByReaderCardNumber(cardNumber);
    }

    public String delete(UUID id) {
        if (!fineRepository.existsById(id)) {
            return "The fine with that id does not exist";
        }
        fineRepository.deleteById(id);
        return "Fine deleted successfully";
    }
}
