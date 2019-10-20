package books.library.boklibrary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    String tag;

    @ManyToMany(mappedBy = "tags")
    Set<Book> books = new HashSet<>();

    public Tag(String tag, Book book) {
        this.tag = tag;
        this.books.add(book);
    }
}
