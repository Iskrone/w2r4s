package me.iskrone.w2r4s.service.specs;

/**
 * Created by Iskander on 25.02.2020
 */
public class SearchBook {
    private String name;
    private String author;
    private String note;
    private String finishingDate;

    private Boolean isDone;
    private Boolean isAudio;

    private Boolean hasPaperBook;

    public SearchBook() {
        this.name = null;
        this.author = null;
        this.finishingDate = null;
        this.note = null;
        this.isDone = null;
        this.isAudio = null;
        this.hasPaperBook = null;
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

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean done) {
        isDone = done;
    }

    public Boolean getIsAudio() {
        return isAudio;
    }

    public void setIsAudio(Boolean audio) {
        isAudio = audio;
    }

    public Boolean getHasPaperBook() {
        return hasPaperBook;
    }

    public void setHasPaperBook(Boolean hasPaperBook) {
        this.hasPaperBook = hasPaperBook;
    }
}
