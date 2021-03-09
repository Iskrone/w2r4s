package me.iskrone.w2r4s.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Iskander on 14.01.2020
 */
@Entity
@Table(name = "BOOKS")
public class Book {
    
    @Id
    @GeneratedValue
    private long id;
    
    private String name;
    private String author;
    private String type;
    private String note;
    private String finishingDate;
    private String extension;

    private boolean isDone;
    private boolean hasPaperBook;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFinishingDate() {
        return finishingDate;
    }

    public void setFinishingDate(String finishingDate) {
        this.finishingDate = finishingDate;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean done) {
        isDone = done;
    }

    public boolean getHasPaperBook() {
        return hasPaperBook;
    }

    public void setHasPaperBook(boolean hasPaperBook) {
        this.hasPaperBook = hasPaperBook;
    }

    public String getHasPaperBookStr() {
        return hasPaperBook ? "Есть" : "Нет";
    }

    public String getIsDoneStr() {
        return isDone ? "Прочитана" : "Нет";
    }

}