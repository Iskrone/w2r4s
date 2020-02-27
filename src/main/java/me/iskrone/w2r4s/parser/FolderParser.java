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
    private static final String DONE = "Прочитано";

    private static FolderParser parser;

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
                String ext = FilenameUtils.getExtension(fileEntry.getName());
                if (Extension.contains(ext)) {
                    Book book = parse(fileEntry.getName(), folder.getName(), ext);
                    if (book != null) {
                        books.add(book);
                    }
                }
            }
        }

        for (File file : foldersQueue) {
            foldersQueue.remove();
            listFilesForFolder(file.getAbsolutePath());
        }
    }

    public Book parse(String fileName, String note, String ext) {
        Book book = null;
        String[] splits = fileName.split(" - ");
        if (splits.length >= 2) {
            book = new Book();
            book.setNote(note);
            if (note.equalsIgnoreCase(DONE) || note.equalsIgnoreCase("!" + DONE)) {
                book.setIsDone(true);
            }
            book.setAuthor(splits[0]);
            book.setName(splits[1].substring(0, splits[1].length() - ext.length() - 1));
        }
        return book;
    }

    public List<File> getFolders() {
        return folders;
    }

    public List<Book> getBooks() {
        return books;
    }
    
    private enum Extension {
        FB2,
        EPUB,
        PDF,
        DJVU,
        DOC,
        RTF;
        
        public static boolean contains(String test) {

            for (Extension extension : Extension.values()) {
                if (extension.name().equalsIgnoreCase(test)) {
                    return true;
                }
            }

            return false;
        }
    }
}
