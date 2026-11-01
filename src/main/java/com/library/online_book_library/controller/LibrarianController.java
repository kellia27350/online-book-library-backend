package com.library.online_book_library.controller;

import com.library.online_book_library.entity.Librarian;
import com.library.online_book_library.service.LibrarianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/librarians")
public class LibrarianController {

    private final LibrarianService librarianService;

    public LibrarianController(LibrarianService librarianService) {
        this.librarianService = librarianService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Librarian librarian) {
        librarianService.save(librarian);
        return ResponseEntity.status(HttpStatus.CREATED).body("Librarian saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(librarianService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var lib = librarianService.getById(id);
        if (lib.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The librarian with that id does not exist");
        }
        return ResponseEntity.ok(lib.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Librarian librarian) {
        var updated = librarianService.update(id, librarian);
        if (updated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The librarian with that id does not exist");
        }
        return ResponseEntity.ok(updated.get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        if (!librarianService.delete(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The librarian with that id does not exist");
        }
        return ResponseEntity.ok("Librarian deleted successfully");
    }

    // search librarians by the genre they hold, name is case insensitive
    @GetMapping("/by-genre")
    public ResponseEntity<?> byGenre(@RequestParam String name) {
        return ResponseEntity.ok(librarianService.getByGenreName(name));
    }

    // adds a genre to a librarian without removing the ones they already have
    @PutMapping("/{librarianId}/genres/{genreId}")
    public ResponseEntity<?> addGenre(@PathVariable UUID librarianId, @PathVariable UUID genreId) {
        String result = librarianService.addGenre(librarianId, genreId);
        if (result.equals("Genre added to the librarian")) {
            return ResponseEntity.ok(result);
        }
        if (result.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    // moves a librarian to a different counter
    @PatchMapping("/{librarianId}/counter")
    public ResponseEntity<?> changeCounter(@PathVariable UUID librarianId, @RequestParam int counterNumber) {
        String result = librarianService.changeCounter(librarianId, counterNumber);
        if (result.equals("Counter changed successfully")) {
            return ResponseEntity.ok(result);
        }
        if (result.contains("does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }
}
