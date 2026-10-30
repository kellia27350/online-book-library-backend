package com.library.online_book_library.service;

import com.library.online_book_library.entity.Genre;
import com.library.online_book_library.entity.Librarian;
import com.library.online_book_library.entity.Reader;
import com.library.online_book_library.repository.CounterRepository;
import com.library.online_book_library.repository.GenreRepository;
import com.library.online_book_library.repository.LibrarianRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LibrarianService {

    private final LibrarianRepository librarianRepository;
    private final GenreRepository genreRepository;
    private final CounterRepository counterRepository;

    public LibrarianService(LibrarianRepository librarianRepository,
                            GenreRepository genreRepository,
                            CounterRepository counterRepository) {
        this.librarianRepository = librarianRepository;
        this.genreRepository = genreRepository;
        this.counterRepository = counterRepository;
    }

    public Librarian save(Librarian librarian) {
        return librarianRepository.save(librarian);
    }

    public List<Librarian> getAll() {
        return librarianRepository.findAll();
    }

    public Optional<Librarian> getById(UUID id) {
        return librarianRepository.findById(id);
    }

    public Optional<Librarian> update(UUID id, Librarian updated) {
        return librarianRepository.findById(id).map(existing -> {
            existing.setFirstName(updated.getFirstName());
            existing.setLastName(updated.getLastName());
            existing.setEmail(updated.getEmail());
            existing.setCounter(updated.getCounter());
            existing.setGenres(updated.getGenres());
            return librarianRepository.save(existing);
        });
    }

    public boolean delete(UUID id) {
        if (librarianRepository.existsById(id)) {
            librarianRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // C1 – librarians by genre name
    public List<Librarian> getByGenreName(String name) {
        return librarianRepository.findByGenreName(name);
    }

    // C4 – distinct readers for a librarian
    public Object getReadersByLibrarianId(UUID librarianId) {
        if (!librarianRepository.existsById(librarianId)) {
            return "The librarian with that id does not exist";
        }
        return librarianRepository.findReadersByLibrarianId(librarianId);
    }

    // D3 – add a genre to a librarian
    public String addGenre(UUID librarianId, UUID genreId) {
        Optional<Librarian> libOpt = librarianRepository.findById(librarianId);
        if (libOpt.isEmpty()) return "The librarian with that id does not exist";

        Optional<Genre> genreOpt = genreRepository.findById(genreId);
        if (genreOpt.isEmpty()) return "That genre does not exist";

        Librarian librarian = libOpt.get();
        Genre genre = genreOpt.get();

        if (librarian.getGenres().contains(genre)) {
            return "The librarian already has that genre";
        }

        librarian.getGenres().add(genre);
        librarianRepository.save(librarian);
        return "Genre added to the librarian";
    }

    // D4 – move librarian to another counter
    public String changeCounter(UUID librarianId, int counterNumber) {
        Optional<Librarian> libOpt = librarianRepository.findById(librarianId);
        if (libOpt.isEmpty()) return "The librarian with that id does not exist";

        var counterOpt = counterRepository.findByCounterNumber(counterNumber);
        if (counterOpt.isEmpty()) return "The counter with that counterNumber does not exist";

        Librarian librarian = libOpt.get();
        var counter = counterOpt.get();

        // check if another librarian already sits there
        boolean taken = librarianRepository.existsByCounter(counter);
        // allow reassignment to the same counter the librarian already has
        if (taken && !counter.equals(librarian.getCounter())) {
            return "That counter is taken by another librarian";
        }

        librarian.setCounter(counter);
        librarianRepository.save(librarian);
        return "Counter changed successfully";
    }
}
