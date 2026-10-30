package com.library.online_book_library.controller;

import com.library.online_book_library.entity.Reader;
import com.library.online_book_library.service.LibrarianService;
import com.library.online_book_library.service.ReaderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;
    private final LibrarianService librarianService;

    public ReaderController(ReaderService readerService, LibrarianService librarianService) {
        this.readerService = readerService;
        this.librarianService = librarianService;
    }

    // A2 – save
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Reader reader) {
        readerService.save(reader);
        return ResponseEntity.status(HttpStatus.CREATED).body("Reader saved successfully");
    }

    // A2 – get all
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(readerService.getAll());
    }

    // A2 – get by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var reader = readerService.getById(id);
        if (reader.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The reader with that id does not exist");
        return ResponseEntity.ok(reader.get());
    }

    // A2 – update
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Reader reader) {
        var updated = readerService.update(id, reader);
        if (updated.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The reader with that id does not exist");
        return ResponseEntity.ok(updated.get());
    }

    // A2 – delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        if (!readerService.delete(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The reader with that id does not exist");
        return ResponseEntity.ok("Reader deleted successfully");
    }

    // B1 – readers by last name, case-insensitive, sorted by firstName
    @GetMapping("/by-last-name")
    public ResponseEntity<?> byLastName(@RequestParam String lastName) {
        return ResponseEntity.ok(readerService.getByLastName(lastName));
    }

    // C4 – readers that have a loan with a given librarian
    @GetMapping("/of-librarian/{librarianId}")
    public ResponseEntity<?> ofLibrarian(@PathVariable UUID librarianId) {
        Object result = librarianService.getReadersByLibrarianId(librarianId);
        if (result instanceof String msg) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(result);
    }
}
