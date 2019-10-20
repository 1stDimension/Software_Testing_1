package books.library.boklibrary.api;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Value
class BookForm {
    @NotBlank
    String title;
    @NotNull
    Integer year;
    @NotNull
    Set<AuthorForm> authors;
    Set<String> tags;
}
