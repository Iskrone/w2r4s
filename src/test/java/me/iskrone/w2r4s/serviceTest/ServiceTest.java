package me.iskrone.w2r4s.serviceTest;

import me.iskrone.w2r4s.Utils;
import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.service.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Добить тесты
 * Created by Iskander on 19.01.2020
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {
    
    @Autowired
    private BookService bookService;
    
    @Before
    public void init() {
        List<Book> books = Utils.initBooks();
        for (Book book : books) {
            book = bookService.save(book);
        }
    }
    
    @After
    public void cleanUp() {
        bookService.clearTable();
    }
    
    public void testGetData() {
        List<Book> books = bookService.getAllBooks();
        
    }
}
