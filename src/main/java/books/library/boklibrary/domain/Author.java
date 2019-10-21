package books.library.boklibrary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String surname;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();

    public void addBook(Book book) {
        books.add(book);
    }


    public Author(@NotBlank String name, @NotBlank String surname) {
        this.name = name;
        this.surname = surname;
    }
}
