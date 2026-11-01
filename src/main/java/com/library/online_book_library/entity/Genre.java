package com.library.online_book_library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    // hiding this from JSON to avoid infinite loop between genre and librarian
    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private List<Librarian> librarians;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Librarian> getLibrarians() { return librarians; }
    public void setLibrarians(List<Librarian> librarians) { this.librarians = librarians; }
}
