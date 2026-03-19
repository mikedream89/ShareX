package com.akansh.fileserversuit.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class OrderingQuestion extends Question {

    private final List<String> originalItems;
    private final List<String> shuffledItems;

    public OrderingQuestion(String stem, List<String> items) {
        super(TYPE_ORDERING, stem);
        this.originalItems = new ArrayList<>(items);
        this.shuffledItems = new ArrayList<>(items);
    }

    public List<String> getOriginalItems() {
        return originalItems;
    }

    public List<String> getShuffledItems() {
        return shuffledItems;
    }

    public void moveItem(int fromPosition, int toPosition) {
        String item = shuffledItems.remove(fromPosition);
        shuffledItems.add(toPosition, item);
    }
}
