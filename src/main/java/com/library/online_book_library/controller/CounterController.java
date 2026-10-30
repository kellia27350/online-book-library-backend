package com.library.online_book_library.controller;

import com.library.online_book_library.entity.Counter;
import com.library.online_book_library.service.CounterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/counters")
public class CounterController {

    private final CounterService counterService;

    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    // A2 – save
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Counter counter) {
        counterService.save(counter);
        return ResponseEntity.status(HttpStatus.CREATED).body("Counter saved successfully");
    }

    // A2 – get all
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(counterService.getAll());
    }

    // A2 – get by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var counter = counterService.getById(id);
        if (counter.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The counter with that id does not exist");
        return ResponseEntity.ok(counter.get());
    }

    // A2 – update
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Counter counter) {
        var updated = counterService.update(id, counter);
        if (updated.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The counter with that id does not exist");
        return ResponseEntity.ok(updated.get());
    }

    // A2 – delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        if (!counterService.delete(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The counter with that id does not exist");
        return ResponseEntity.ok("Counter deleted successfully");
    }

    // C2 – counters with no librarian
    @GetMapping("/free")
    public ResponseEntity<?> free() {
        return ResponseEntity.ok(counterService.getFreeCounters());
    }
}
