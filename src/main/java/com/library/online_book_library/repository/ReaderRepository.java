package com.library.online_book_library.repository;

import com.library.online_book_library.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReaderRepository extends JpaRepository<Reader, UUID> {

    List<Reader> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    Optional<Reader> findByCardNumber(String cardNumber);
}
