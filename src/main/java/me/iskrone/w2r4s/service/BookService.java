package me.iskrone.w2r4s.service;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Iskander on 14.01.2020
 */
@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    public void save(Book book) {
        bookRepository.save(book);
    }
    
    /*
    Поиск:
    1. По автору
    2. По названию
    3. По дате
    4. По прочитанным
    5. По аудио
    6. Слово в комментарии
     */
}
