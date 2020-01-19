package me.iskrone.w2r4s.service;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.service.specs.BookSpecification;
import me.iskrone.w2r4s.repository.BookRepository;
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
        return bookRepository.save(book);
    }
    
    public void saveBunch(List<Book> books) {
        bookRepository.saveAll(books);
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
    
    public Page<Book> getFilteredBooksWithPagination(Book searchBook, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        long size = bookRepository.count();

        List<Book> bookList = bookRepository.findAllWithPagination(
                Specification.where(BookSpecification.withBookNameIn(searchBook.getName())).
                        and(BookSpecification.withAuthorIn(searchBook.getAuthor())).
                        and(BookSpecification.withFinishingDate(searchBook.getFinishingDate())).
                        and(BookSpecification.withHasPaperBook(searchBook.isHasPaperBook())).
                        and(BookSpecification.withIsAudio(searchBook.isAudio())).
                        and(BookSpecification.withIsDone(searchBook.isDone())),
                pageRequest).getContent();

        return new PageImpl<>(bookList, PageRequest.of(currentPage, pageSize), size);
    }
}
