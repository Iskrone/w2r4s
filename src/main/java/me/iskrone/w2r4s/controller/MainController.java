package me.iskrone.w2r4s.controller;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Iskander on 14.01.2020
 */
@Controller
public class MainController {
    @Autowired
    private BookService bookService;

    @RequestMapping(value = "/")
    public String listBooks(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Book book = new Book();

        Page<Book> bookPage = bookService.getFilteredBooksWithPagination(book, 
                PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("bookPage", bookPage);

        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "index.html";
    }
}
