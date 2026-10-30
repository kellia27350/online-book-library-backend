package com.library.online_book_library.repository;

import com.library.online_book_library.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReaderRepository extends JpaRepository<Reader, UUID> {

    // B1 – readers by last name, case-insensitive, sorted A→Z by firstName
    List<Reader> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    // E2 – look up reader by card number
    Optional<Reader> findByCardNumber(String cardNumber);
}
