package books.library.boklibrary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Year;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String title;
    @NotNull
    private Integer year;

    @ManyToMany
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    private boolean rented;

    @ManyToOne
    @JoinColumn
    private LibraryUser rentedBy;

    public void rent(LibraryUser user) {
        if (!rented) {
            rented = true;
            rentedBy = user;
        } else {
            throw new IllegalArgumentException("Book is already rented");
        }
    }

    public void returnn(LibraryUser user) {
        if (rented && Objects.nonNull(rentedBy) && rentedBy.equals(user)) {
            rented = false;
            rentedBy = null;
        } else {
            throw new IllegalArgumentException(String.format("Book is not rented by %s", user.getUsername()));
        }
    }

    public Book(@NotBlank String title, @NotNull Integer year, Set<Author> authors, Set<String> tags) {
        this.title = title;
        this.year = year;
        this.authors = authors;
        this.tags = tags.stream().map(it -> new Tag(it, this)).collect(Collectors.toSet());
    }
}
