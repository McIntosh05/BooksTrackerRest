package ua.defatov.bookstrackerrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.defatov.bookstrackerrest.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
