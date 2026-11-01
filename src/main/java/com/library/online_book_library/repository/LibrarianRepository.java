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

    boolean existsByCounter(Counter counter);

    @Query("SELECT l FROM Librarian l JOIN l.genres g WHERE LOWER(g.name) = LOWER(:name)")
    List<Librarian> findByGenreName(@Param("name") String name);

    @Query("SELECT DISTINCT lo.reader FROM Loan lo WHERE lo.librarian.id = :librarianId")
    List<Reader> findReadersByLibrarianId(@Param("librarianId") UUID librarianId);
}
