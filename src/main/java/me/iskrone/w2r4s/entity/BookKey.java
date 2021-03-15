package me.iskrone.w2r4s.entity;

import java.io.Serializable;

/**
 * Created by Iskander on 11.03.2021
 */
public class BookKey implements Serializable {
    private String name;
    private String author;
    private String type;
    private String extension;

    public BookKey() {
    }
    
    public BookKey(String name, String author, String type, String extension) {
        this.name = name;
        this.author = author;
        this.type = type;
        this.extension = extension;
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
