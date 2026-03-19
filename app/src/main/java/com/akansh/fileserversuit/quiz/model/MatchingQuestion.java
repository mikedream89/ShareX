package com.akansh.fileserversuit.quiz.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingQuestion extends Question {

    private final List<String> leftItems;
    private final List<String> rightItems;
    private final Map<Integer, Integer> matchMap = new HashMap<>();

    public MatchingQuestion(String stem, List<String> leftItems, List<String> rightItems) {
        super(TYPE_MATCHING, stem);
        this.leftItems = leftItems;
        this.rightItems = rightItems;
    }

    public List<String> getLeftItems() {
        return leftItems;
    }

    public List<String> getRightItems() {
        return rightItems;
    }

    public Map<Integer, Integer> getMatchMap() {
        return matchMap;
    }

    public void setMatch(int leftIndex, int rightIndex) {
        matchMap.put(leftIndex, rightIndex);
    }

    public int getMatchedRight(int leftIndex) {
        Integer right = matchMap.get(leftIndex);
        return right != null ? right : -1;
    }

    public void clearMatch(int leftIndex) {
        matchMap.remove(leftIndex);
    }
}
