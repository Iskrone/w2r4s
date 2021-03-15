package me.iskrone.w2r4s.serviceTest;

import me.iskrone.w2r4s.Utils;
import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.exceptions.W2ROperationFailedException;
import me.iskrone.w2r4s.ie.Uploader;
import me.iskrone.w2r4s.service.BookService;
import me.iskrone.w2r4s.service.specs.SearchBook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Iskander on 14.01.2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SaveBookTest {

    private static final String TEST_XLSX = "src/test/java/me/iskrone/w2r4s/data/TestBooks.xlsx";
    private static final String TEST_BOOK_TYPE = "TEST_TEST_TEST";
    
    @Autowired
    private BookService bookService;

    @Test
    public void testAddBook() throws W2ROperationFailedException {
        Book testBook1 = new Book();
        testBook1.setAuthor("Федор Достоевский");
        testBook1.setName("Братья Карамазовы");
        testBook1.setIsDone(true);
        testBook1.setNote("Отличная книга!");
        testBook1.setExtension("fb2");
        testBook1.setType(TEST_BOOK_TYPE);
        testBook1.setFinishingDate("2013");
        testBook1.setHasPaperBook(false);
        testBook1 = bookService.save(testBook1);

        Book checkTB1 = bookService.getBook(testBook1.getName(), testBook1.getAuthor(),
                testBook1.getType(), testBook1.getExtension());
        Assert.assertEquals(checkTB1.getName(), testBook1.getName());
        Assert.assertEquals(checkTB1.getAuthor(), testBook1.getAuthor());
        Assert.assertEquals(checkTB1.getFinishingDate(), testBook1.getFinishingDate());
        Assert.assertEquals(checkTB1.getType(), testBook1.getType());
        Assert.assertEquals(checkTB1.getIsDone(), testBook1.getIsDone());
        Assert.assertEquals(checkTB1.getExtension(), testBook1.getExtension());
        Assert.assertEquals(checkTB1.getHasPaperBook(), testBook1.getHasPaperBook());
        Assert.assertEquals(checkTB1.getNote(), testBook1.getNote());
    }

    @Test
    public void testIsExistedBook() {
        Book testBook1 = new Book();
        testBook1.setAuthor("Достоевский Федор");
        testBook1.setName("Братья Карамазовы");
        testBook1.setIsDone(true);
        testBook1.setNote("Отличная книга!");
        testBook1.setExtension("fb2");
        testBook1.setType(TEST_BOOK_TYPE);
        testBook1.setFinishingDate("2013");
        testBook1.setHasPaperBook(false);
        testBook1 = bookService.save(testBook1);
        
        boolean exist = bookService.isBookExist(testBook1);
        Assert.assertTrue(exist);
    }

    @Test
    public void testHalfBook() throws W2ROperationFailedException {
        final String testAuthor = "Пушкин Александр1";
        try {
            List<Book> list = bookService.getAllBooks();
            int size = list.size();
            final String testName = "Медный Всадник1";
            Book testBook1 = new Book();
            testBook1.setAuthor(testAuthor);
            testBook1.setName(testName);
            testBook1.setIsDone(false);
            testBook1.setNote("");
            testBook1.setExtension("");
            testBook1.setType("");
            testBook1.setFinishingDate("");
            testBook1.setHasPaperBook(false);
            testBook1 = bookService.save(testBook1);

            list = bookService.getAllBooks();
            Assert.assertEquals(size + 1, list.size());
            Book checkTB1 = bookService.getBook(testBook1.getName(), testBook1.getAuthor(),
                    testBook1.getType(), testBook1.getExtension());
            Assert.assertEquals(testName, checkTB1.getName());
            Assert.assertEquals(testAuthor, checkTB1.getAuthor());
        } finally {
            SearchBook searchBook = new SearchBook();
            searchBook.setAuthor(testAuthor);
            bookService.deleteBooks(bookService.getFilteredBooks(searchBook));
        }
    }
    
    @Test
    public void testUpdateBook() throws W2ROperationFailedException {
        List<Book> list = bookService.getAllBooks();
        int size = list.size();
        Book testBook1 = new Book();
        testBook1.setAuthor("Достоевский Федор");
        testBook1.setName("Братья Карамазовы");
        testBook1.setIsDone(true);
        testBook1.setNote("Отличная книга!");
        testBook1.setExtension("fb2");
        testBook1.setType(TEST_BOOK_TYPE);
        testBook1.setFinishingDate("2013");
        testBook1.setHasPaperBook(false);
        testBook1 = bookService.save(testBook1);
        list = bookService.getAllBooks();
        Assert.assertEquals(size + 1, list.size());
        Book checkTB1 = bookService.getBook(testBook1.getName(), testBook1.getAuthor(), 
                testBook1.getType(), testBook1.getExtension());
        
        Book testBook2 = new Book();
        testBook2.setAuthor("Достоевский Федор");
        testBook2.setName("Братья Карамазовы");
        testBook2.setExtension("pdf");
        testBook2.setType(TEST_BOOK_TYPE);
        testBook2.setIsDone(true);
        testBook2.setNote("Отличная книга!");
        testBook2.setFinishingDate("2013");
        testBook2.setHasPaperBook(false);
        testBook2 = bookService.save(testBook2);
        list = bookService.getAllBooks();
        Assert.assertEquals(size + 2, list.size());
        
        Book checkTB11 = bookService.getBook(testBook1.getName(), testBook1.getAuthor(),
                testBook1.getType(), testBook1.getExtension());
        Assert.assertNotNull(checkTB11);
        Book checkTB2 = bookService.getBook(testBook2.getName(), testBook2.getAuthor(),
                testBook2.getType(), testBook2.getExtension());
        Assert.assertNotEquals(checkTB1.getExtension(), checkTB2.getExtension());
    }
    
    @Test
    public void testParseExcel() throws IOException, ParseException {
        bookService.clearTable();
        File xlsx = new File(TEST_XLSX);
        InputStream targetStream = new FileInputStream(xlsx);
        Uploader uploader = null;
        try {
            long initSize = bookService.getAllBooks().size();
            uploader = new Uploader(targetStream);
            List<Book> parsedBooks = uploader.parseBooks();
            int parsedSize = parsedBooks.size();
            long uploadedSize = bookService.saveBunch(parsedBooks);
            Assert.assertEquals(parsedSize, uploadedSize);

            List<Book> books = bookService.getAllBooks();
            Assert.assertEquals(initSize + parsedSize, books.size());

            int i = 0;
            for (Book b1 : parsedBooks) {
                for (Book bDB : books) {
                    if (bDB.getName().equalsIgnoreCase(b1.getName()) &&
                            bDB.getAuthor().equalsIgnoreCase(b1.getAuthor())) {
                        Assert.assertEquals(b1.getNote(), bDB.getNote());
                        Assert.assertEquals(b1.getType(), bDB.getType());
                        Assert.assertEquals(b1.getFinishingDate(), bDB.getFinishingDate());
                        Assert.assertEquals(b1.getHasPaperBook(), bDB.getHasPaperBook());
                        Assert.assertEquals(b1.getExtension(), bDB.getExtension());
                        Assert.assertEquals(b1.getIsDone(), bDB.getIsDone());
                        i++;
                    }
                }
            }
            Assert.assertEquals(parsedSize, i);
        } finally {
            if (uploader != null) {
                uploader.close();
            }
        }
    }

    @Test
    public void testDeleteBooks() {
        int startSize = bookService.getAllBooks().size();
        List<Book> books = Utils.initBooks();

        long booksSizeAfterSave = bookService.saveBunch(books);
        Assert.assertEquals(startSize + books.size(), booksSizeAfterSave);
        
        SearchBook searchBook = new SearchBook();
        searchBook.setType(TEST_BOOK_TYPE);
        
        List<Book> filteredBooks = bookService.getFilteredBooks(searchBook);
        int filteredSize1 = filteredBooks.size();
        bookService.deleteBook(filteredBooks.get(0));
        filteredBooks = bookService.getFilteredBooks(searchBook);
        Assert.assertEquals(filteredSize1 - 1, filteredBooks.size());
        bookService.deleteBooks(filteredBooks);
        filteredBooks = bookService.getFilteredBooks(searchBook);
        Assert.assertEquals(0, filteredBooks.size());
    }
    
    @After
    public void cleanUp() {
        SearchBook searchBook = new SearchBook();
        searchBook.setType(TEST_BOOK_TYPE);
        List<Book> list = bookService.getFilteredBooks(searchBook);
        bookService.deleteBooks(list);
    }
}
