package ua.defatov.bookstrackerrest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {

    private Long id;

    private String title;

    private String author;

    private String genre;

    private byte grade;

    private String description;

}
