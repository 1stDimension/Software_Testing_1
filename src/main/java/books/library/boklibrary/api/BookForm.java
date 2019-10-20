package books.library.boklibrary.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
class BookForm {
    @NotBlank
    private String title;
    @NotNull
    private Integer year;
    @NotNull
    private Set<AuthorForm> authors;
    private Set<String> tags;
}
