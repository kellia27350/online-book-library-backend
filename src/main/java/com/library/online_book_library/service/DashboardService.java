package com.library.online_book_library.service;

import com.library.online_book_library.entity.LoanStatus;
import com.library.online_book_library.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final ReaderRepository readerRepository;
    private final LibrarianRepository librarianRepository;
    private final CounterRepository counterRepository;
    private final LoanRepository loanRepository;
    private final FineRepository fineRepository;

    public DashboardService(ReaderRepository readerRepository,
                            LibrarianRepository librarianRepository,
                            CounterRepository counterRepository,
                            LoanRepository loanRepository,
                            FineRepository fineRepository) {
        this.readerRepository = readerRepository;
        this.librarianRepository = librarianRepository;
        this.counterRepository = counterRepository;
        this.loanRepository = loanRepository;
        this.fineRepository = fineRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("readers", readerRepository.count());
        summary.put("librarians", librarianRepository.count());
        summary.put("freeCounters", (long) counterRepository.findFreeCounters().size());
        summary.put("loansToday", loanRepository.countByLoanDate(LocalDate.now()));
        summary.put("requested", loanRepository.countByStatus(LoanStatus.REQUESTED));
        summary.put("fines", fineRepository.count());
        return summary;
    }
}
