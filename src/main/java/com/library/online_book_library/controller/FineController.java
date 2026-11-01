package com.library.online_book_library.controller;

import com.library.online_book_library.entity.Fine;
import com.library.online_book_library.service.FineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Fine fine, @RequestParam UUID loanId) {
        Object result = fineService.save(fine, loanId);
        if (result instanceof String msg) {
            if (msg.contains("does not exist")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Fine saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(fineService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var fine = fineService.getById(id);
        if (fine.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The fine with that id does not exist");
        }
        return ResponseEntity.ok(fine.get());
    }

    @GetMapping("/reader/{cardNumber}")
    public ResponseEntity<?> byReader(@PathVariable String cardNumber) {
        return ResponseEntity.ok(fineService.getByReaderCardNumber(cardNumber));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        String result = fineService.delete(id);
        if (result.equals("Fine deleted successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
}
