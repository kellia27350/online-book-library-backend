package com.library.online_book_library.repository;

import com.library.online_book_library.entity.Counter;
import com.library.online_book_library.entity.Librarian;
import com.library.online_book_library.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface LibrarianRepository extends JpaRepository<Librarian, UUID> {

    // D4 – check if any librarian already sits at that counter
    boolean existsByCounter(Counter counter);

    // C1 – librarians who hold a genre (case-insensitive)
    @Query("SELECT l FROM Librarian l JOIN l.genres g WHERE LOWER(g.name) = LOWER(:name)")
    List<Librarian> findByGenreName(@Param("name") String name);

    // C4 – distinct readers that have a loan with this librarian
    @Query("SELECT DISTINCT lo.reader FROM Loan lo WHERE lo.librarian.id = :librarianId")
    List<Reader> findReadersByLibrarianId(@Param("librarianId") UUID librarianId);
}
