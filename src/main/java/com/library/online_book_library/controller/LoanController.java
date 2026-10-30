package com.library.online_book_library.controller;

import com.library.online_book_library.entity.Loan;
import com.library.online_book_library.entity.LoanStatus;
import com.library.online_book_library.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // D1 – safe save (also covers A2)
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Loan loan) {
        Object result = loanService.save(loan);
        if (result instanceof String msg) {
            if (msg.contains("past")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Loan saved successfully");
    }

    // A2 – get all
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(loanService.getAll());
    }

    // A2 – get by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var loan = loanService.getById(id);
        if (loan.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The loan with that id does not exist");
        return ResponseEntity.ok(loan.get());
    }

    // A2 – general update (does NOT touch status)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Loan loan) {
        var updated = loanService.update(id, loan);
        if (updated.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The loan with that id does not exist");
        return ResponseEntity.ok(updated.get());
    }

    // E3 – protected delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String result = loanService.delete(id);
        if (result.equals("Loan deleted successfully")) return ResponseEntity.ok(result);
        if (result.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    // B2 – loans by status
    @GetMapping("/by-status")
    public ResponseEntity<?> byStatus(@RequestParam LoanStatus status) {
        return ResponseEntity.ok(loanService.getByStatus(status));
    }

    // B3 – loans between two dates
    @GetMapping("/between")
    public ResponseEntity<?> between(@RequestParam String start, @RequestParam String end) {
        LocalDate from = LocalDate.parse(start);
        LocalDate to   = LocalDate.parse(end);
        return ResponseEntity.ok(loanService.getBetween(from, to));
    }

    // D2 – status flow
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id, @RequestParam LoanStatus status) {
        String result = loanService.changeStatus(id, status);
        if (result.equals("Loan status updated successfully")) return ResponseEntity.ok(result);
        if (result.contains("does not exist")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // F1 – loans per status
    @GetMapping("/stats/by-status")
    public ResponseEntity<?> statsByStatus() {
        return ResponseEntity.ok(loanService.getStatsByStatus());
    }

    // F2 – bulk cancel a librarian's loans on a date
    @PatchMapping("/cancel-day")
    public ResponseEntity<?> cancelDay(@RequestParam UUID librarianId, @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return ResponseEntity.ok(loanService.cancelDay(librarianId, parsedDate));
    }

    // Bonus – paginated loans
    @GetMapping("/page")
    public ResponseEntity<Page<Loan>> page(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "loanDate,desc") String sort) {
        String[] parts = sort.split(",");
        Sort.Direction dir = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, parts[0]));
        return ResponseEntity.ok(loanService.getPage(pageable));
    }
}
