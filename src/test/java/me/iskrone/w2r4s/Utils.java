package me.iskrone.w2r4s;

import me.iskrone.w2r4s.entity.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iskander on 18.01.2020
 */
public class Utils {
    public static final String NAME_1 = "Федор Достоевский";

    public static List<Book> initBooks() {
        List<Book> books = new ArrayList<>();

        Book testBook1 = new Book();
        testBook1.setAuthor(NAME_1);
        testBook1.setName("Братья Карамазовы");
        testBook1.setDone(true);
        testBook1.setNote("Отличная книга!");
        testBook1.setAudio(false);
        testBook1.setFinishingDate("2013");
        testBook1.setHasPaperBook(false);
        books.add(testBook1);

        Book testBook2 = new Book();
        testBook2.setAuthor("Джон Перкинс");
        testBook2.setName("Исповедь экономического убийцы");
        testBook2.setDone(false);
        testBook2.setNote("Должно быть интересно");
        testBook2.setAudio(true);
        testBook2.setFinishingDate(null);
        testBook2.setHasPaperBook(false);
        books.add(testBook2);

        Book testBook3 = new Book();
        testBook3.setAuthor("Роберт Льюис Стивенсон");
        testBook3.setName("Остров сокровищ");
        testBook3.setDone(true);
        testBook3.setNote("Любимая книга детства");
        testBook3.setAudio(false);
        testBook3.setFinishingDate("До 2012");
        testBook3.setHasPaperBook(true);
        books.add(testBook3);

        return books;
    }
}
