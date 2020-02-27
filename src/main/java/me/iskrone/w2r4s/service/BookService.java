package me.iskrone.w2r4s.service;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.service.specs.BookSpecification;
import me.iskrone.w2r4s.repository.BookRepository;
import me.iskrone.w2r4s.service.specs.SearchBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.hibernate.criterion.Restrictions.and;

/**
 * Created by Iskander on 14.01.2020
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book save(Book book) {
        if (!isBookExist(book)) {
            return bookRepository.save(book);
        }
        return null;
    }

    private boolean isBookExist(Book book) {
        String[] author = book.getAuthor().split(" ");
        StringBuilder author1 = new StringBuilder();
        StringBuilder author2 = new StringBuilder();
        int size = author.length;
        for (String namePart : author) {
            size--;
            author1.append(namePart).append(" ");
            author2.append(author[size]).append(" ");
        }
        String a1 = author1.toString().substring(0, author1.toString().length() - 1);
        String a2 = author2.toString().substring(0, author2.toString().length() - 1);

        if (bookRepository.findOne(
                Specification.where(BookSpecification.withBookNameIn(book.getName())).and(
                        BookSpecification.withAuthorIn(a1))).isPresent()) {
            return true;
        }

        if (bookRepository.findOne(
                Specification.where(BookSpecification.withBookNameIn(book.getName())).and(
                        BookSpecification.withAuthorIn(a2))).isPresent()) {
            return true;
        }

        return false;
    }

    public void saveBunch(List<Book> books) {
        bookRepository.saveAll(books);
    }

    public Long saveBunchOneByOne(List<Book> books) {
        long count = 0;
        for (Book book : books) {
            Book result = save(book);
            count++;
            if (result == null) {
                count--;
            }
        }

        return count;
    }

    public void removeBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book getBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        //TODO: Переделать на Exception: Book not found
        return book.isPresent() ? book.get() : null;
    }

    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Page<Book> getBooksWithPagination(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        long size = bookRepository.count();

        List<Book> bookList = bookRepository.findAll(pageRequest).getContent();
        return new PageImpl<>(bookList, PageRequest.of(currentPage, pageSize), size);
    }

    public Page<Book> getFilteredBooksWithPagination(SearchBook searchBook, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        long size = bookRepository.count();

        List<Book> bookList = bookRepository.findAll(
                Specification.where(searchBook.getName() == null ?
                        Specification.where(null) :
                        BookSpecification.withBookNameIn(searchBook.getName())).
                        and(searchBook.getAuthor() == null ?
                                Specification.where(null) :
                                BookSpecification.withAuthorIn(searchBook.getAuthor())).
                        and(searchBook.getFinishingDate() == null ?
                                Specification.where(null) :
                                BookSpecification.withFinishingDate(searchBook.getFinishingDate())).
                        and(searchBook.getHasPaperBook() == null ?
                                Specification.where(null) :
                                BookSpecification.withHasPaperBook(searchBook.getHasPaperBook())).
                        and(searchBook.getIsAudio() == null ?
                                Specification.where(null) :
                                BookSpecification.withIsAudio(searchBook.getIsAudio())).
                        and(searchBook.getIsDone() == null ?
                                Specification.where(null) :
                                BookSpecification.withIsDone(searchBook.getIsDone())),
                pageRequest).getContent();

        return new PageImpl<>(bookList, PageRequest.of(currentPage, pageSize), size);
    }

    public void clearTable() {
        bookRepository.deleteAll();
    }
}
