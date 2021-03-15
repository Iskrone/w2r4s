package me.iskrone.w2r4s.controller;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.forms.FilterForm;
import me.iskrone.w2r4s.forms.PathForm;
import me.iskrone.w2r4s.ie.Uploader;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
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
        int pageSize = size.orElse(20);
        Page<Book> bookPage;
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
            if (pathForm.getDefaultDateStr() != null) {
                parser.addDefaultData(pathForm.getDefaultDateStr());
            }
            parser.parsePath(pathForm.getPath());
            Long count = bookService.saveBunchOneByOne(parser.getBooks());
            model.addAttribute("result", count);
        }
        return "collect";
    }

    @RequestMapping(value = "/uploadFile")
    public String upload(ModelMap modelMap) {
        modelMap.addAttribute("booksFromFile", 0);
        return "uploadFile";
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, 
                         @RequestParam("isClearBooks") Boolean isClearBooks, 
                         ModelMap modelMap) throws IOException, ParseException {
        Long count = 0L;
        if (file.isEmpty()) {
            modelMap.addAttribute("file", file);
            if (isClearBooks) {
                bookService.clearTable();
            }
            Uploader uploader = new Uploader(file.getInputStream());
            List<Book> books = uploader.parseBooks();
            count = bookService.saveBunch(books);
        }
        modelMap.addAttribute("booksFromFile", count);
        return "uploadFile";
    }

    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) throws IOException {
        response.setHeader("Content-disposition", "attachment; filename=Books.xlsx");
        ArrayList<Book> allBooks = (ArrayList<Book>) bookService.getAllBooks();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("My Books");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Автор");
        header.createCell(1).setCellValue("Имя книги");
        header.createCell(2).setCellValue("Информация");
        header.createCell(3).setCellValue("Дата прочтения");
        header.createCell(4).setCellValue("Закончена");
        header.createCell(5).setCellValue("Формат");
        header.createCell(6).setCellValue("Есть бумажная книга");
        header.createCell(7).setCellValue("Примечания");

        int rowNum = 1;
        for (Book book : allBooks) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(book.getAuthor());
            row.createCell(1).setCellValue(book.getName());
            row.createCell(2).setCellValue(book.getType());
            row.createCell(3).setCellValue(book.getFinishingDate());
            row.createCell(4).setCellValue(book.getIsDoneStr());
            row.createCell(5).setCellValue(book.getExtension());
            row.createCell(6).setCellValue(book.getHasPaperBookStr());
            row.createCell(7).setCellValue(book.getNote());
        }
        workbook.write(response.getOutputStream());
    }
}
