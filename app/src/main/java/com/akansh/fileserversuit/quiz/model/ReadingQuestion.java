package com.akansh.fileserversuit.quiz.model;

import java.util.List;

public class ReadingQuestion extends Question {

    private final String articleTitle;
    private final String articleContent;
    private final List<Question> subQuestions;

    public ReadingQuestion(String stem, String articleTitle, String articleContent, List<Question> subQuestions) {
        super(TYPE_READING, stem);
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.subQuestions = subQuestions;
        for (int i = 0; i < subQuestions.size(); i++) {
            subQuestions.get(i).setNumber(i + 1);
        }
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public List<Question> getSubQuestions() {
        return subQuestions;
    }
}
