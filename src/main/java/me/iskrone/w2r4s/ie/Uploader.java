package me.iskrone.w2r4s.ie;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.service.BookService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Iskander on 05.01.2021
 */
public class Uploader {
    
    private XSSFWorkbook myExcelBook;
    private List<Book> books2Add = new ArrayList<>();
    
    public Uploader(InputStream file) throws IOException {
        this.myExcelBook = new XSSFWorkbook(file);
    }
    
    public List<Book> parseBooks() throws IOException, ParseException {
        XSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
        for (Row row : myExcelSheet) {
            if (row.getRowNum() == 0) {
                // Пропускаем заголовки
                continue;
            }

            Book book = new Book();
            Cell cell; 
            book.setAuthor(row.getCell(0) == null ? "" : row.getCell(0).getStringCellValue());
            book.setName(row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue());
            book.setType(row.getCell(2) == null ? "" : row.getCell(2).getStringCellValue());
            String strValue;
            cell = row.getCell(3);
            if (cell != null && cell.getCellType().equals(CellType.NUMERIC)) {
                Date date = cell.getDateCellValue();
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                strValue = format.format(date);
            } else {
                DataFormatter formatter = new DataFormatter();
                strValue = formatter.formatCellValue(cell);
            }
            book.setFinishingDate(strValue);
            book.setIsDone(row.getCell(4) != null && parseYesOrNo(row.getCell(4).getStringCellValue()));
            book.setExtension(row.getCell(5) == null ? "" : row.getCell(5).getStringCellValue());
            book.setHasPaperBook(row.getCell(6) != null && parseYesOrNo(row.getCell(6).getStringCellValue()));
            book.setNote(row.getCell(7) == null ? "" : row.getCell(7).getStringCellValue());
            books2Add.add(book);
        }
        
        return books2Add;
    }
    
    private boolean parseYesOrNo(String value) {
        if (value.equalsIgnoreCase("да")) {
            return true;
        }
        if (value.equalsIgnoreCase("нет")) {
            return false;
        }
        
        return false;
    }
    
    public void close() throws IOException {
        myExcelBook.close();        
    } 
}
