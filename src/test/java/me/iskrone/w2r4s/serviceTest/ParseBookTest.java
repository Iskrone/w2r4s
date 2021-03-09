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

/**
 * Created by Iskander on 25.01.2020
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ParseBookTest {
    private static final String TEST_FOLDER_1 = "src/test/java/me/iskrone/w2r4s/data/testFolder/insideFolder2/";
    private static final String TEST_FOLDER_MAIN = "src/test/java/me/iskrone/w2r4s/data/";

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
        parser.parsePath(TEST_FOLDER_1);

        Book book = parser.getBooks().get(0);
        Assert.assertEquals(testAuthorName, book.getAuthor());
        Assert.assertEquals(testBookName, book.getName());
        Assert.assertEquals(1, parser.getBooks().size());
        Assert.assertEquals(0, parser.getFolders().size());
    }

    @Test
    public void testRealPath() {
        FolderParser parser = FolderParser.getInstance();
        parser.parsePath(TEST_FOLDER_MAIN);
        Assert.assertEquals(8, parser.getBooks().size());
        Assert.assertEquals(5, parser.getFolders().size());
    }

    @Test
    public void checkSaveBooks() {
        FolderParser parser = FolderParser.getInstance();
        parser.parsePath(TEST_FOLDER_MAIN);
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
        bookService.clearTable();
    }
}
