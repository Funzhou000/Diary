package com.diary.entity;

import java.io.Serializable;

public class Diary implements Serializable {
    // 2. 建议添加 serialVersionUID（可选，但是推荐的做法）
    private static final long serialVersionUID = 1L;
    private String title;
    private String content;
    private String id;

    public Diary(String title, String content, String id) {
        this.title = title;
        this.content = content;
        this.id = id;
    }

    public Diary() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
