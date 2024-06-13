package com.example.stride_todoapp;

import java.util.List;

public class Task {
    private String title;
    private List<ContentLine> contentLines;

    public Task(String title, List<ContentLine> contentLines) {
        this.title = title;
        this.contentLines = contentLines;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ContentLine> getContentLines() {
        return contentLines;
    }

    public void setContentLines(List<ContentLine> contentLines) {
        this.contentLines = contentLines;
    }

    public static class ContentLine {
        private String text;
        private boolean isChecked;

        public ContentLine(String text, boolean isChecked) {
            this.text = text;
            this.isChecked = isChecked;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
