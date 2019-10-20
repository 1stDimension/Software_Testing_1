package books.library.boklibrary.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
class AuthorForm {
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
}
