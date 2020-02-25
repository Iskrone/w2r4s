package me.iskrone.w2r4s.serviceTest;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.service.BookService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iskander on 14.01.2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SaveBookTest {

    @Autowired
    private BookService bookService;
    private List<Long> testBooksIds = new ArrayList<>();

    @Test
    public void testAddBook() {
        Book testBook1 = new Book();
        testBook1.setAuthor("Федор Достоевский");
        testBook1.setName("Братья Карамазовы");
        testBook1.setIsDone(true);
        testBook1.setNote("Отличная книга!");
        testBook1.setIsAudio(false);
        testBook1.setFinishingDate("2013");
        testBook1.setHasPaperBook(false);
        testBook1 = bookService.save(testBook1);
        testBooksIds.add(testBook1.getId());

        Book checkTB1 = bookService.getBook(testBook1.getId());
        Assert.assertEquals(checkTB1.getName(), testBook1.getName());
        Assert.assertEquals(checkTB1.getAuthor(), testBook1.getAuthor());
        Assert.assertEquals(checkTB1.getFinishingDate(), testBook1.getFinishingDate());
        Assert.assertEquals(checkTB1.getNote(), testBook1.getNote());
        Assert.assertEquals(checkTB1.getIsDone(), testBook1.getIsDone());
        Assert.assertEquals(checkTB1.getIsAudio(), testBook1.getIsAudio());
        Assert.assertEquals(checkTB1.getHasPaperBook(), testBook1.getHasPaperBook());
    }

    @Test
    public void testAddExistedBook() {
        Book testBook1 = null;
        try {
            testBook1 = new Book();
            testBook1.setAuthor("Достоевский Федор");
            testBook1.setName("Братья Карамазовы");
            testBook1.setIsDone(true);
            testBook1.setNote("Отличная книга!");
            testBook1.setIsAudio(false);
            testBook1.setFinishingDate("2013");
            testBook1.setHasPaperBook(false);
            testBook1 = bookService.save(testBook1);
            Assert.assertNull(testBook1);
        } finally {
            if (testBook1 != null) {
                bookService.removeBook(testBook1.getId());
            }
        }
    }

//    @After
//    public void cleanUp() {
//        for (Long id : testBooksIds) {
//            bookService.removeBook(id);
//        }
//    }
}
