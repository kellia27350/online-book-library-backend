package com.library.online_book_library.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "librarian")
public class Librarian {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;

    // a librarian sits at one counter, counter_id is the foreign key
    @ManyToOne
    @JoinColumn(name = "counter_id")
    private Counter counter;

    // librarian owns this relationship, the join table is librarian_genre
    // initialized as empty list so .contains() never throws NullPointerException
    @ManyToMany
    @JoinTable(name = "librarian_genre")
    private List<Genre> genres = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Counter getCounter() { return counter; }
    public void setCounter(Counter counter) { this.counter = counter; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }
}
