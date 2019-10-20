package books.library.boklibrary.api;

import books.library.boklibrary.domain.Author;
import books.library.boklibrary.domain.AuthorRepository;
import books.library.boklibrary.domain.Book;
import books.library.boklibrary.domain.BookRepository;
import books.library.boklibrary.domain.LibraryUser;
import books.library.boklibrary.domain.TagRepository;
import books.library.boklibrary.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@RequestMapping("/books")
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;

    @GetMapping
    List<BookDto> findAll(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "tags", required = false) List<String> tags,
            @RequestBody(required = false) @Valid AuthorForm author
    ) {
        Set<Book> books = new HashSet<>();
        if (Strings.isNotBlank(title) || Objects.nonNull(tags) || Objects.nonNull(author)) {
            if (Strings.isNotBlank(title)) {
                books.addAll(bookRepository.findAllByTitle(title));
            }
            if (Objects.nonNull(tags)) {
                books.addAll(bookRepository.findAllByTagsIn(tagRepository.findAllById(tags)));
            }
            if (Objects.nonNull(author)) {
                books.addAll(bookRepository.findAllByAuthorsIn(authorRepository.findAllByNameAndSurname(author.getName(), author.getSurname())));
            }
        } else {
            books = new HashSet<>(bookRepository.findAll());
        }
        return mapToDto(books);
    }

    @Transactional
    @PatchMapping(path = "/{bookId}/rent")
    HttpStatus rent(@PathVariable long bookId) {
        try {
            bookRepository.findById(bookId).ifPresent(book -> {
                LibraryUser loggedUser = retrieveLoggedUser();
                book.rent(loggedUser);
                bookRepository.save(book);
            });
            return HttpStatus.ACCEPTED;
        } catch (UsernameNotFoundException | IllegalArgumentException ex) {
            log.info(ex);
            return HttpStatus.METHOD_NOT_ALLOWED;
        }
    }

    @Transactional
    @PatchMapping(path = "/{bookId}/return")
    HttpStatus returnn(@PathVariable long bookId) {
        try {
            bookRepository.findById(bookId).ifPresent(book -> {
                LibraryUser loggedUser = retrieveLoggedUser();
                book.returnn(loggedUser);
                bookRepository.save(book);
            });
            return HttpStatus.ACCEPTED;
        } catch (UsernameNotFoundException | IllegalArgumentException ex) {
            log.info(ex);
            return HttpStatus.METHOD_NOT_ALLOWED;
        }
    }

    @Transactional
    @PostMapping
    BookDto add(@Valid BookForm bookForm) {
        Set<Author> authors = new HashSet<>();
        bookForm.getAuthors().forEach(authorForm -> {
            List<Author> existing = authorRepository.findAllByNameAndSurname(authorForm.getName(), authorForm.getSurname());
            if (existing.isEmpty()) {
                Author newAuthor = new Author(authorForm.getName(), authorForm.getSurname());
                authors.add(newAuthor);
            } else {
                authors.addAll(existing);
            }
        });
        Book newBook = new Book(bookForm.getTitle(), bookForm.getYear(), authors, bookForm.getTags());
        authorRepository.saveAll(authors);
        return BookDto.from(bookRepository.save(newBook));
    }

    @Transactional
    @DeleteMapping(path = "/{bookId}")
    void delete(@PathVariable long bookId) {
        bookRepository.deleteById(bookId);
    }

    private List<BookDto> mapToDto(Set<Book> books) {
        return books.stream().map(BookDto::from).collect(Collectors.toList());
    }

    private LibraryUser retrieveLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
    }
}
