package com.akansh.fileserversuit.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class FillBlankQuestion extends Question {

    private final String textWithBlanks;
    private final int blankCount;
    private final List<String> answers;

    public FillBlankQuestion(String stem, String textWithBlanks, int blankCount) {
        super(TYPE_FILL_BLANK, stem);
        this.textWithBlanks = textWithBlanks;
        this.blankCount = blankCount;
        this.answers = new ArrayList<>();
        for (int i = 0; i < blankCount; i++) {
            answers.add("");
        }
    }

    public String getTextWithBlanks() {
        return textWithBlanks;
    }

    public int getBlankCount() {
        return blankCount;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswer(int index, String answer) {
        if (index >= 0 && index < answers.size()) {
            answers.set(index, answer);
        }
    }
}
