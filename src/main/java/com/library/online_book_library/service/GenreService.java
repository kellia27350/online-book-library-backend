package com.library.online_book_library.service;

import com.library.online_book_library.entity.Genre;
import com.library.online_book_library.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    public Optional<Genre> getById(UUID id) {
        return genreRepository.findById(id);
    }

    public Optional<Genre> update(UUID id, Genre updated) {
        return genreRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            return genreRepository.save(existing);
        });
    }

    public boolean delete(UUID id) {
        if (genreRepository.existsById(id)) {
            genreRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // C3 – genres nobody holds
    public List<Genre> getUnusedGenres() {
        return genreRepository.findUnusedGenres();
    }
}
