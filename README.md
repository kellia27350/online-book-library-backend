# Online Book Library – Backend API

**Student:** Igiraneza Mahoro Kellia  
**Student ID:** 27350  
**Course:** INSY 8322 – Web Technology and Internet  
**Project:** Online Book Library (Mid-Term Examination)

---

## What this project does

A REST API built with Spring Boot that manages an online book library — readers, librarians, counters, genres, loans and fines. Every business rule from the exam paper is implemented, including status flow enforcement, fine protection and bulk updates.

---

## Tech stack

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- PostgreSQL
- Maven

---

## How to run

### 1. Create the database

Open pgAdmin or psql and run:

```sql
CREATE DATABASE online_book_library;
```

### 2. Update credentials if needed

Open `src/main/resources/application.properties` and set your PostgreSQL username and password:

```properties
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3. Build and start the API

```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080`. Hibernate creates all tables automatically on first run.

### 4. Seed the data

Use Postman to POST the seed data through the save endpoints (see Step 0 in the exam paper). Start in this order so foreign keys resolve correctly:

1. `POST /api/genres/save` — create Fiction, Science, History, Poetry
2. `POST /api/counters/save` — create counters 1, 2, 3, 4
3. `POST /api/librarians/save` — create 3 librarians, each with a counter
4. `POST /api/readers/save` — create 5 readers (two with lastName Mahoro)
5. `POST /api/loans/save` — create 10 loans across October–November 2026

---

## All endpoints

### Genres — `/api/genres`

| Method | Path | What it does |
|--------|------|--------------|
| POST | `/save` | Save a genre |
| GET | `/all` | List all genres |
| GET | `/{id}` | Get genre by id |
| PUT | `/update/{id}` | Update a genre |
| DELETE | `/delete/{id}` | Delete a genre |
| GET | `/unused` | Genres no librarian holds (C3) |

### Counters — `/api/counters`

| Method | Path | What it does |
|--------|------|--------------|
| POST | `/save` | Save a counter |
| GET | `/all` | List all counters |
| GET | `/{id}` | Get counter by id |
| PUT | `/update/{id}` | Update a counter |
| DELETE | `/delete/{id}` | Delete a counter |
| GET | `/free` | Counters with no librarian (C2) |

### Librarians — `/api/librarians`

| Method | Path | What it does |
|--------|------|--------------|
| POST | `/save` | Save a librarian |
| GET | `/all` | List all librarians |
| GET | `/{id}` | Get librarian by id |
| PUT | `/update/{id}` | Update a librarian |
| DELETE | `/delete/{id}` | Delete a librarian |
| GET | `/by-genre?name=fiction` | Librarians by genre name (C1) |
| PUT | `/{librarianId}/genres/{genreId}` | Add genre to librarian (D3) |
| PATCH | `/{librarianId}/counter?counterNumber=4` | Move to another counter (D4) |

### Readers — `/api/readers`

| Method | Path | What it does |
|--------|------|--------------|
| POST | `/save` | Save a reader |
| GET | `/all` | List all readers |
| GET | `/{id}` | Get reader by id |
| PUT | `/update/{id}` | Update a reader |
| DELETE | `/delete/{id}` | Delete a reader |
| GET | `/by-last-name?lastName=mahoro` | Readers by last name (B1) |
| GET | `/of-librarian/{librarianId}` | Readers linked to a librarian (C4) |

### Loans — `/api/loans`

| Method | Path | What it does |
|--------|------|--------------|
| POST | `/save` | Safe save with business checks (D1) |
| GET | `/all` | List all loans |
| GET | `/{id}` | Get loan by id |
| PUT | `/update/{id}` | Update a loan (status excluded) |
| DELETE | `/delete/{id}` | Delete loan, blocked if fines exist (E3) |
| GET | `/by-status?status=REQUESTED` | Loans by status (B2) |
| GET | `/between?start=2026-10-01&end=2026-10-31` | Loans in date range (B3) |
| PATCH | `/{id}/status?status=ISSUED` | Status flow (D2) |
| GET | `/stats/by-status` | Count per status (F1) |
| PATCH | `/cancel-day?librarianId=...&date=2026-10-20` | Bulk cancel (F2) |
| GET | `/page?page=0&size=5&sort=loanDate,desc` | Paginated loans (Bonus) |

### Fines — `/api/fines`

| Method | Path | What it does |
|--------|------|--------------|
| POST | `/save?loanId=...` | Write a fine for a RETURNED loan (E1) |
| GET | `/all` | List all fines |
| GET | `/{id}` | Get fine by id |
| GET | `/reader/{cardNumber}` | Fine history for a reader (E2) |
| DELETE | `/delete/{id}` | Delete a fine (E3) |

### Dashboard (Bonus) — `/api/dashboard`

| Method | Path | What it does |
|--------|------|--------------|
| GET | `/summary` | Counts for readers, librarians, free counters, loans today, requested, fines |

---

## HTTP status codes used

| Code | Meaning |
|------|---------|
| 200 | OK — success |
| 201 | Created — save successful |
| 400 | Bad Request — validation or status flow error |
| 404 | Not Found — id does not exist |
| 409 | Conflict — duplicate or protected resource |

---

## Postman collection

Import `online-book-library-midterm.postman_collection.json` from the root of this repository. Each request is named by its requirement ID (A2, B1, C4, D2, ...) and includes at least one success case and one error case.
