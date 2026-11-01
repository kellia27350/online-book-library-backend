package com.library.online_book_library.repository;

import com.library.online_book_library.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {

    @Query("SELECT g FROM Genre g WHERE g.librarians IS EMPTY")
    List<Genre> findUnusedGenres();
}
