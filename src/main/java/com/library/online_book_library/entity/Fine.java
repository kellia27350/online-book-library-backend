package com.library.online_book_library.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fine")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String reason;
    private String note;
    private int amount;
    private LocalDate chargedDate;

    // fine belongs to a loan, loan_id is the foreign key
    // I do not add a List<Fine> back to Loan to keep things simple
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public LocalDate getChargedDate() { return chargedDate; }
    public void setChargedDate(LocalDate chargedDate) { this.chargedDate = chargedDate; }

    public Loan getLoan() { return loan; }
    public void setLoan(Loan loan) { this.loan = loan; }
}
