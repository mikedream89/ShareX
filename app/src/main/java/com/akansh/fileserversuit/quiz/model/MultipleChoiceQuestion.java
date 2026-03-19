package com.akansh.fileserversuit.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuestion extends Question {

    private final List<String> options;
    private final List<Integer> selectedIndices = new ArrayList<>();

    public MultipleChoiceQuestion(String stem, List<String> options) {
        super(TYPE_MULTIPLE_CHOICE, stem);
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getSelectedIndices() {
        return selectedIndices;
    }

    public void toggleSelection(int index) {
        if (selectedIndices.contains(index)) {
            selectedIndices.remove(Integer.valueOf(index));
        } else {
            selectedIndices.add(index);
        }
    }

    public boolean isSelected(int index) {
        return selectedIndices.contains(index);
    }
}
