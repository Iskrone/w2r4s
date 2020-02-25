package me.iskrone.w2r4s.parser;

import me.iskrone.w2r4s.entity.Book;
import me.iskrone.w2r4s.service.BookService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.*;

/**
 * Created by Iskander on 25.01.2020
 */
public class FolderParser {

    private static FolderParser parser;

    @Autowired
    BookService bookService;

    private Queue<File> foldersQueue = new PriorityQueue<>();
    private List<File> folders = new ArrayList<>();
    private List<Book> books = new ArrayList<>();

    private FolderParser() {
    }

    public static FolderParser getInstance() {
        if (parser != null) {
            return parser;
        }
        parser = new FolderParser();
        return parser;
    }

    public void listFilesForFolder(String path) {
        File folder = new File(path);
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                foldersQueue.add(fileEntry);
                folders.add(fileEntry);
            } else {
                books.add(parse(fileEntry.getName(), folder.getName()));
            }
        }

        for (File file : foldersQueue) {
            foldersQueue.remove();
            listFilesForFolder(file.getAbsolutePath());
        }
    }

    public Book parse(String fileName, String note) {
        Book book = new Book();
        book.setNote(note);
        String[] splits = fileName.split(" - ");
        book.setAuthor(splits[0]);
        String ext = FilenameUtils.getExtension(fileName);
        book.setName(splits[1].substring(0, splits[1].length() - ext.length() - 1));
        return book;
    }

    public List<File> getFolders() {
        return folders;
    }

    public List<Book> getBooks() {
        return books;
    }
}
