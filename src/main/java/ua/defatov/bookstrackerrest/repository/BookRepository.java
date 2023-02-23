package ua.defatov.bookstrackerrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.defatov.bookstrackerrest.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
