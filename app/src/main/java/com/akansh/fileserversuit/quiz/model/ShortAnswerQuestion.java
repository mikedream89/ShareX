package com.akansh.fileserversuit.quiz.model;

public class ShortAnswerQuestion extends Question {

    private String userAnswer = "";

    public ShortAnswerQuestion(String stem) {
        super(TYPE_SHORT_ANSWER, stem);
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
