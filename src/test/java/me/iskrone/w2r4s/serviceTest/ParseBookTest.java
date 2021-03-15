package me.iskrone.w2r4s.serviceTest;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.parser.FolderParser;
import me.iskrone.w2r4s.service.BookService;
import me.iskrone.w2r4s.service.specs.SearchBook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by Iskander on 25.01.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ParseBookTest {
    private static final String TEST_FOLDER_PATH = "src/test/java/me/iskrone/w2r4s/data/testFolder/";
    private static final String ROOT_FOLDER_PATH = "src/test/java/me/iskrone/w2r4s/";
    private static final String DATA_FOLDER_NAME = "data";
    private static final String INSIDE_FOLDER_1_NAME = "insideFolder1";
    private static final String INSIDE_FOLDER_2_NAME = "insideFolder2";

    @Autowired
    private BookService bookService;

    @Test
    public void parseBook() {
        String testAuthorName = "Богданов Александр";
        String testBookName = "Инженер Мэнни";
        String type = "00 Художественные";
        String ext = "fb2";

        FolderParser parser = FolderParser.getInstance();

        Book book = parser.buildBookFromFilename("Богданов Александр - Инженер Мэнни.fb2",
                "00 Художественные", "fb2");
        Assert.assertEquals(testAuthorName, book.getAuthor());
        Assert.assertEquals(testBookName, book.getName());
        Assert.assertEquals(type, book.getType());
        Assert.assertEquals(ext, book.getExtension());

        Book book2 = parser.buildBookFromFilename("01. Дэн Аббнет - Возвышение Хоруса.fb2",
                "Ересь Хоруса", "fb2");
        testAuthorName = "Дэн Аббнет";
        testBookName = "Возвышение Хоруса";
        type = "Ересь Хоруса";
        Assert.assertEquals(testAuthorName, book2.getAuthor());
        Assert.assertEquals(testBookName, book2.getName());
        Assert.assertEquals(type, book2.getType());
        Assert.assertEquals(ext, book2.getExtension());
    }

    @Test
    public void parseFolder() {
        String testFileName = "Валё Пер - Гибель 31-го отдела";
        String[] splits = testFileName.split(" - ");
        String testAuthorName = splits[0];
        String testBookName = splits[1];

        FolderParser parser = FolderParser.getInstance();
        parser.parsePath(TEST_FOLDER_PATH + INSIDE_FOLDER_2_NAME);

        Book book = parser.getBooks().get(0);
        Assert.assertEquals(testAuthorName, book.getAuthor());
        Assert.assertEquals(testBookName, book.getName());
        Assert.assertEquals(1, parser.getBooks().size());
        Assert.assertEquals(0, parser.getFolders().size());
    }

    @Test
    public void testRealPath() {
        FolderParser parser = FolderParser.getInstance();
        parser.parsePath(ROOT_FOLDER_PATH + DATA_FOLDER_NAME);
        Assert.assertEquals(8, parser.getBooks().size());
        Assert.assertEquals(5, parser.getFolders().size());
    }

    @Test
    public void checkSaveBooks() {
        FolderParser parser = FolderParser.getInstance();
        parser.parsePath(ROOT_FOLDER_PATH + DATA_FOLDER_NAME);
        Assert.assertEquals(8, parser.getBooks().size());
        Assert.assertEquals(5, parser.getFolders().size());
        bookService.saveBunchOneByOne(parser.getBooks());

        SearchBook book = new SearchBook();
        book.setAuthor("Гоголь");
        book.setName("Мертвые души");
        Page<Book> bookPage = bookService.getFilteredBooksWithPagination(book,
                PageRequest.of(0, 10));
        Book bookFound = bookPage.getContent().get(0);
        Assert.assertEquals(book.getName(), bookFound.getName());
        Assert.assertEquals("Гоголь Николай", bookFound.getAuthor());
    }

    @After
    public void cleanUp() {
        SearchBook searchBook = new SearchBook();
        searchBook.setType(DATA_FOLDER_NAME);
        List<Book> list = bookService.getFilteredBooks(searchBook);
        searchBook = new SearchBook();
        searchBook.setType(INSIDE_FOLDER_1_NAME);
        list.addAll(bookService.getFilteredBooks(searchBook));
        searchBook = new SearchBook();
        searchBook.setType(INSIDE_FOLDER_2_NAME);
        list.addAll(bookService.getFilteredBooks(searchBook));
        bookService.deleteBooks(list);
    }
}
