package com.library.online_book_library.repository;

import com.library.online_book_library.entity.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CounterRepository extends JpaRepository<Counter, UUID> {

    Optional<Counter> findByCounterNumber(int counterNumber);

    @Query("SELECT c FROM Counter c WHERE NOT EXISTS (SELECT l FROM Librarian l WHERE l.counter = c) ORDER BY c.counterNumber")
    List<Counter> findFreeCounters();
}
