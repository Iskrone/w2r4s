package me.iskrone.w2r4s.controller;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.forms.FilterForm;
import me.iskrone.w2r4s.forms.PathForm;
import me.iskrone.w2r4s.parser.FolderParser;
import me.iskrone.w2r4s.service.BookService;
import me.iskrone.w2r4s.service.specs.SearchBook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
            @ModelAttribute FilterForm filterForm,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Book> bookPage = null;
        if ((filterForm.getAuthorFilter() != null && !filterForm.getAuthorFilter().equals("")) ||
                (filterForm.getNameFilter() != null && !filterForm.getNameFilter().equals(""))) {
            SearchBook book = new SearchBook();
            book.setAuthor(filterForm.getAuthorFilter());
            book.setName(filterForm.getNameFilter());
            bookPage = bookService.getFilteredBooksWithPagination(book,
                    PageRequest.of(currentPage - 1, pageSize));
        } else {
            bookPage = bookService.getBooksWithPagination(PageRequest.of(currentPage - 1, pageSize));
        }

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

    @RequestMapping(value = "/collect")
    public String parseFolders(Model model, @ModelAttribute PathForm pathForm) {
        if (pathForm.getPath() != null) {
            FolderParser parser = FolderParser.getInstance();
            parser.listFilesForFolder(pathForm.getPath());
            Long count = bookService.saveBunchOneByOne(parser.getBooks());
            model.addAttribute("result", count);
        }
        return "collect";
    }

    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) throws IOException {
        response.setHeader("Content-disposition", "attachment; filename=Books.xlsx");
        ArrayList<Book> allBooks = (ArrayList<Book>) bookService.getAllBooks();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("My Books");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Автор");
        header.createCell(2).setCellValue("Имя книги");
        header.createCell(3).setCellValue("Информация");
        header.createCell(4).setCellValue("Дата прочтения");
        header.createCell(5).setCellValue("Закончена");
        header.createCell(6).setCellValue("Есть аудио");
        header.createCell(7).setCellValue("Есть бумажная книга");
        
        int rowNum = 1;
        for (Book book : allBooks) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(book.getId());
            row.createCell(1).setCellValue(book.getAuthor());
            row.createCell(2).setCellValue(book.getName());
            row.createCell(3).setCellValue(book.getNote());
            row.createCell(4).setCellValue(book.getFinishingDate());
            row.createCell(5).setCellValue(book.getIsDoneStr());
            row.createCell(6).setCellValue(book.getIsAudioStr());
            row.createCell(7).setCellValue(book.getHasPaperBookStr());
        }
        workbook.write(response.getOutputStream());
    }
}
