package com.library.online_book_library.service;

import com.library.online_book_library.entity.Reader;
import com.library.online_book_library.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;

    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public Reader save(Reader reader) {
        return readerRepository.save(reader);
    }

    public List<Reader> getAll() {
        return readerRepository.findAll();
    }

    public Optional<Reader> getById(UUID id) {
        return readerRepository.findById(id);
    }

    public Optional<Reader> update(UUID id, Reader updated) {
        return readerRepository.findById(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setCardNumber(updated.getCardNumber());
            existing.setEmail(updated.getEmail());
            return readerRepository.save(existing);
        });
    }

    public boolean delete(UUID id) {
        if (readerRepository.existsById(id)) {
            readerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // B1 – readers by last name, case-insensitive, sorted by firstName
    public List<Reader> getByLastName(String lastName) {
        return readerRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }
}
