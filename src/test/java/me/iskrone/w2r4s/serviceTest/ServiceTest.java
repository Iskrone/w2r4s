package me.iskrone.w2r4s.serviceTest;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.service.BookService;
import org.junit.After;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iskander on 14.01.2020
 */
public class ServiceTest {
    private BookService bookService;
    private List<Long> testBooksIds = new ArrayList<>();
    
    public void initBooks() {
        
    }
    
    public void testAddBook() {
        Book testBook1 = new Book();
        testBook1.setAuthor("Федор Достоевский");
        testBook1.setName("Братья Карамазовы");
        testBook1.setDone(true);
        testBook1.setNote("Отличная книга!");
        testBook1.setAudio(false);
        testBook1.setFinishingDate("2013");
        testBook1.setHasPaperBook(false);
        testBook1 = bookService.save(testBook1);
        testBooksIds.add(testBook1.getId());
        
        Book testBook2 = new Book();
        testBook2.setAuthor("Джон Перкинс");
        testBook2.setName("Исповедь экономического убийцы");
        testBook2.setDone(false);
        testBook2.setNote("Должно быть интересно");
        testBook2.setAudio(true);
        testBook2.setFinishingDate(null);
        testBook2.setHasPaperBook(false);
        testBook2 = bookService.save(testBook2);
        testBooksIds.add(testBook2.getId());

        Book testBook3 = new Book();
        testBook3.setAuthor("Роберт Льюис Стивенсон");
        testBook3.setName("Остров сокровищ");
        testBook3.setDone(true);
        testBook3.setNote("Любимая книга детства");
        testBook3.setAudio(false);
        testBook3.setFinishingDate("До 2012");
        testBook3.setHasPaperBook(true);
        testBook3 = bookService.save(testBook3);
        testBooksIds.add(testBook3.getId());
        
        Book checkTB1 = bookService.getBook(testBook1.getId());
        Assert.assertEquals(checkTB1.getName(), testBook1.getName());
        Assert.assertEquals(checkTB1.getAuthor(), testBook1.getAuthor());
        Assert.assertEquals(checkTB1.getFinishingDate(), testBook1.getFinishingDate());
        Assert.assertEquals(checkTB1.getNote(), testBook1.getNote());
        Assert.assertEquals(checkTB1.isDone(), testBook1.isDone());
        Assert.assertEquals(checkTB1.isAudio(), testBook1.isAudio());
        Assert.assertEquals(checkTB1.isHasPaperBook(), testBook1.isHasPaperBook());
    }
    
    @After
    public void cleanUp() {
        for (Long id : testBooksIds) {
            bookService.removeBook(id);
        }
    }
}
