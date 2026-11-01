package com.library.online_book_library.service;

import com.library.online_book_library.entity.Counter;
import com.library.online_book_library.repository.CounterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CounterService {

    private final CounterRepository counterRepository;

    public CounterService(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    public Counter save(Counter counter) {
        return counterRepository.save(counter);
    }

    public List<Counter> getAll() {
        return counterRepository.findAll();
    }

    public Optional<Counter> getById(UUID id) {
        return counterRepository.findById(id);
    }

    public Optional<Counter> update(UUID id, Counter updated) {
        return counterRepository.findById(id).map(existing -> {
            existing.setCounterNumber(updated.getCounterNumber());
            existing.setName(updated.getName());
            return counterRepository.save(existing);
        });
    }

    public boolean delete(UUID id) {
        if (counterRepository.existsById(id)) {
            counterRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Counter> getFreeCounters() {
        return counterRepository.findFreeCounters();
    }
}
