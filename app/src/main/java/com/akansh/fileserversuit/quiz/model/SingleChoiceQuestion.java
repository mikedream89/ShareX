package com.akansh.fileserversuit.quiz.model;

import java.util.List;

public class SingleChoiceQuestion extends Question {

    private final List<String> options;
    private int selectedIndex = -1;

    public SingleChoiceQuestion(String stem, List<String> options) {
        super(TYPE_SINGLE_CHOICE, stem);
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
