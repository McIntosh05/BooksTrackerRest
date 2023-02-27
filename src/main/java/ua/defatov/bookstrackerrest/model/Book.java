package ua.defatov.bookstrackerrest.model;

import lombok.*;
import ua.defatov.bookstrackerrest.exceptions.InvalidValueException;

import javax.persistence.*;

@Entity
@Table(name = "books")
@Getter
@Setter()
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    @Setter(AccessLevel.NONE)
    private byte grade;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void setGrade(byte grade) {
        if(grade <= 10 && grade >= 0) {
            this.grade = grade;
        }
        else {
            throw new InvalidValueException("An Invalid Number was entered");
        }
    }
}
