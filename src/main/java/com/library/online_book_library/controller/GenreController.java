package com.library.online_book_library.controller;

import com.library.online_book_library.entity.Genre;
import com.library.online_book_library.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // A2 – save
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Genre genre) {
        Genre saved = genreService.save(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body("Genre saved successfully");
    }

    // A2 – get all
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(genreService.getAll());
    }

    // A2 – get by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var genre = genreService.getById(id);
        if (genre.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The genre with that id does not exist");
        return ResponseEntity.ok(genre.get());
    }

    // A2 – update
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Genre genre) {
        var updated = genreService.update(id, genre);
        if (updated.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The genre with that id does not exist");
        return ResponseEntity.ok(updated.get());
    }

    // A2 – delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        if (!genreService.delete(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The genre with that id does not exist");
        return ResponseEntity.ok("Genre deleted successfully");
    }

    // C3 – genres nobody holds
    @GetMapping("/unused")
    public ResponseEntity<?> unused() {
        return ResponseEntity.ok(genreService.getUnusedGenres());
    }
}
