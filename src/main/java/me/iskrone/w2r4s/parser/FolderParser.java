package me.iskrone.w2r4s.parser;

import me.iskrone.w2r4s.entity.Book;
import org.apache.commons.io.FilenameUtils;

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
    private long countSeemsBookFiles = 0;
    private String defaultDateStr = "";

    private FolderParser() {
    }

    public static FolderParser getInstance() {
        if (parser != null) {
            return parser;
        }
        parser = new FolderParser();
        return parser;
    }

    public void parsePath(String path) {
        countSeemsBookFiles = 0;
        foldersQueue = new PriorityQueue<>();
        folders = new ArrayList<>();
        books = new ArrayList<>();
        listFilesForFolder(path);
    }
    
    public void addDefaultData(String defaultDate) {
        this.defaultDateStr = defaultDate;
    }

    private void listFilesForFolder(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            for (final File fileEntry : files) {
                if (!fileEntry.getName().startsWith("-")) {
                    if (fileEntry.isDirectory()) {
                        String extension = FilenameUtils.getExtension(fileEntry.getName());
                        if (BookExtension.HTML.name().equalsIgnoreCase(extension) ||
                                BookExtension.COMIX.name().equalsIgnoreCase(extension)) {
                            addBookFromFilename(fileEntry, folder);
                        } else {
                            if (BookExtension.AUDIO.name().equalsIgnoreCase(extension)) {
                                addBookFromFilename(fileEntry, folder);
                            }
                            foldersQueue.add(fileEntry);
                        }
                        folders.add(fileEntry);
                    } else {
                        addBookFromFilename(fileEntry, folder);
                    }
                }
            }
        }

        for (File file : foldersQueue) {
            foldersQueue.remove();
            listFilesForFolder(file.getAbsolutePath());
        }
    }

    private void addBookFromFilename(final File fileEntry, File folder) {
        String ext = FilenameUtils.getExtension(fileEntry.getName());
        if (BookExtension.contains(ext)) {
            countSeemsBookFiles++;
            Book book = buildBookFromFilename(fileEntry.getName(), folder.getName(), ext);
            if (book != null) {
                books.add(book);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }

    public Book buildBookFromFilename(String fileName, String type, String ext) {
        Book book = null;
        String[] splits = fileName.split(" - ");
        if (splits.length >= 2) {
            book = new Book();
            book.setType(type);
            boolean isBookDoneV1 = type.toUpperCase().startsWith(DONE.toUpperCase());
            boolean isBookDoneV2 = type.toUpperCase().startsWith(("!" + DONE).toUpperCase());
            if (isBookDoneV1 || isBookDoneV2) {
                book.setIsDone(true);
                book.setFinishingDate(defaultDateStr);
            }
            String[] authorNameSplits = splits[0].split("\\. ");
            if (isNumber(authorNameSplits[0])) {
                book.setAuthor(splits[0].substring(authorNameSplits[0].length() + 2, splits[0].length()));
            } else {
                book.setAuthor(splits[0]);
            }
            book.setName(splits[1].substring(0, splits[1].length() - ext.length() - 1));
            book.setExtension(ext);
        }
        return book;
    }

    private Boolean isNumber(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public List<File> getFolders() {
        return folders;
    }

    public List<Book> getBooks() {
        return books;
    }

    public long getCountSeemsBookFiles() {
        return countSeemsBookFiles;
    }
    
    private enum BookExtension {
        FB2,
        EPUB,
        PDF,
        DJVU,
        DOC,
        RTF,
        TXT,
        MOBI,
        PDO,
        CBR,
        COMIX,
        AUDIO,
        HTML;

        public static boolean contains(String test) {
            for (BookExtension extension : BookExtension.values()) {
                if (extension.name().equalsIgnoreCase(test)) {
                    return true;
                }
            }

            return false;
        }
    }
}
