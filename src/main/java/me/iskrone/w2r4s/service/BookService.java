package me.iskrone.w2r4s.service;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.exceptions.W2ROperationFailedException;
import me.iskrone.w2r4s.repository.BookRepository;
import me.iskrone.w2r4s.service.specs.BookSpecification;
import me.iskrone.w2r4s.service.specs.SearchBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Сервис для работы с книгами
 * Created by Iskander on 14.01.2020
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public boolean isBookExist(Book book) {
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
                Specification.where(BookSpecification.withBookName(book.getName())).and(
                        BookSpecification.withAuthor(a1))).isPresent()) {
            return true;
        }

        if (bookRepository.findOne(
                Specification.where(BookSpecification.withBookName(book.getName())).and(
                        BookSpecification.withAuthor(a2))).isPresent()) {
            return true;
        }

        return false;
    }

    public long saveBunch(List<Book> books) {
        Iterable<Book> savedBooks = bookRepository.saveAll(books);
        return StreamSupport.stream(savedBooks.spliterator(), false).count();
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

    public Book getBook(String name, String author, 
                        String type, String extension) throws W2ROperationFailedException {
        List<Book> books = bookRepository.findAll(
                Specification.where(name == null ? Specification.where(null) : BookSpecification.withBookNameIn(name)).
                        and(author == null ? Specification.where(null) : BookSpecification.withAuthorIn(author)).
                        and(extension == null ? Specification.where(null) : BookSpecification.withExtension(extension)).
                        and(type == null ? Specification.where(null) : BookSpecification.withTypeIn(type)));
        
        if (books.size() == 0) {
            return null;
        } else if (books.size() > 1) {
            throw new W2ROperationFailedException("There is multiple books with this " +
                    "name (" + name + ") " + "author (" + author + ") " + 
                    "type (" + type + ") " + "extension (" + extension + ") " +
                    "- " + books.size());
        }

        return books.get(0);
    }
    
    public List<Book> getAllBooks() {
        return getFilteredBooks(null);
    }

    /**
     * Получить все книги с фильтрацией, отстортированы по автору
     * @param searchBook данные для фильтрации
     * @return все книги с фильтрацией, отстортированы по автору
     */
    public List<Book> getFilteredBooks(SearchBook searchBook) {
        return bookRepository.findAll(createBookSpec(searchBook), Sort.by(Sort.Direction.ASC, Book.AUTHOR_DB_FIELD));
    }

    /**
     * Получить все книги с фильтрацией, отстортированы по автору и с пейджированием
     * @param searchBook данные для фильтрации
     * @param pageable пейджирование
     * @return все книги с фильтрацией, отстортированы по автору и с пейджированием
     */
    public Page<Book> getFilteredBooksWithPagination(SearchBook searchBook, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        Sort sort = Sort.by(Sort.Direction.ASC, Book.AUTHOR_DB_FIELD);
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);
        long size = bookRepository.count();

        List<Book> bookList = bookRepository.findAll(createBookSpec(searchBook), pageRequest).getContent();

        return new PageImpl<>(bookList, PageRequest.of(currentPage, pageSize), size);
    }

    public void clearTable() {
        bookRepository.deleteAll();
    }
    
    public void deleteBooks(List<Book> books) {
        bookRepository.deleteAll(books);
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    private Specification<Book> createBookSpec(SearchBook searchBook) {
        if (searchBook == null) {
            return Specification.where(null);
        }
        return Specification.where(searchBook.getName() == null ?
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
                and(searchBook.getExtension() == null ?
                        Specification.where(null) :
                        BookSpecification.withExtension(searchBook.getExtension())).
                and(searchBook.getType() == null ?
                        Specification.where(null) :
                        BookSpecification.withTypeIn(searchBook.getType())).
                and(searchBook.getIsDone() == null ?
                        Specification.where(null) :
                        BookSpecification.withIsDone(searchBook.getIsDone()));
    }
}
