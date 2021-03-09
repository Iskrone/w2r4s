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
    private int countFiles = 0;
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
        countFiles = 0;
        foldersQueue = new PriorityQueue<>();
        folders = new ArrayList<>();
        books = new ArrayList<>();
        listFilesForFolder(path);
        System.out.println(countFiles);
    }
    
    public void addDefaultData(String defaultDate) {
        this.defaultDateStr = defaultDate;
    }

    private void listFilesForFolder(String path) {
        File folder = new File(path);
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
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

        for (File file : foldersQueue) {
            foldersQueue.remove();
            listFilesForFolder(file.getAbsolutePath());
        }
    }

    private void addBookFromFilename(final File fileEntry, File folder) {
        String ext = FilenameUtils.getExtension(fileEntry.getName());
        if (BookExtension.contains(ext)) {
            Book book = buildBookFromFilename(fileEntry.getName(), folder.getName(), ext);
            if (book != null) {
                books.add(book);
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
            if (authorNameSplits.length == 2) {
                book.setAuthor(authorNameSplits[1]);
            } else {
                book.setAuthor(splits[0]);
            }
            book.setName(splits[1].substring(0, splits[1].length() - ext.length() - 1));
            book.setExtension(ext);
        }
        return book;
    }

    public List<File> getFolders() {
        return folders;
    }

    public List<Book> getBooks() {
        return books;
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

        private static BookExtension getExtension(String extName) {
            for (BookExtension extension : BookExtension.values()) {
                if (extension.name().equalsIgnoreCase(extName)) {
                    return extension;
                }
            }

            throw new NoSuchElementException();
        }

        public static boolean isFolderExt(String test) {
            if (contains(test)) {
                BookExtension testExt = getExtension(test);
                switch (testExt) {
                    case COMIX:
                        return true;
                    case AUDIO:
                        return true;
                    case HTML:
                        return true;
                    default:
                        return false;
                }
            }

            return false;
        }
    }
}
