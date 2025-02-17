package me.iskrone.w2r4s;

import me.iskrone.w2r4s.entity.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iskander on 18.01.2020
 */
public class Utils {
    public static final String AUTHOR_1 = "Федор Достоевский1";
    private static final String TEST_BOOK_TYPE = "TEST_TEST_TEST";

    public static List<Book> initBooks() {
        List<Book> books = new ArrayList<>();

        Book testBook1 = new Book();
        testBook1.setAuthor(AUTHOR_1);
        testBook1.setName("Братья Карамазовы");
        testBook1.setIsDone(true);
        testBook1.setNote("Отличная книга!");
        testBook1.setExtension("fb2");
        testBook1.setType(TEST_BOOK_TYPE);
        testBook1.setFinishingDate("2013");
        testBook1.setHasPaperBook(false);
        books.add(testBook1);

        Book testBook2 = new Book();
        testBook2.setAuthor("Джон Перкинс");
        testBook2.setName("Исповедь экономического убийцы");
        testBook2.setIsDone(false);
        testBook2.setNote("Должно быть интересно");
        testBook2.setExtension("audio");
        testBook2.setType(TEST_BOOK_TYPE);
        testBook2.setFinishingDate(null);
        testBook2.setHasPaperBook(false);
        books.add(testBook2);

        Book testBook3 = new Book();
        testBook3.setAuthor("Роберт Льюис Стивенсон");
        testBook3.setName("Остров сокровищ");
        testBook3.setIsDone(true);
        testBook3.setNote("С картинками");
        testBook3.setExtension("pdf");
        testBook3.setType(TEST_BOOK_TYPE);
        testBook3.setFinishingDate("До 2012");
        testBook3.setHasPaperBook(true);
        books.add(testBook3);

        return books;
    }
}
