package com.library.online_book_library.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "counter")
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int counterNumber;

    private String name;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public int getCounterNumber() { return counterNumber; }
    public void setCounterNumber(int counterNumber) { this.counterNumber = counterNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
