package me.iskrone.w2r4s.entity;

import javax.persistence.*;

/**
 * Created by Iskander on 14.01.2020
 */
@Entity
@Table(name = "BOOKS")
@IdClass(BookKey.class)
public class Book {

    @Id
    private String name;
    
    @Id
    private String author;

    @Id
    private String type;

    @Id
    private String extension;

    private String note;
    private String finishingDate;
    private boolean isDone;
    private boolean hasPaperBook;

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