package com.akansh.fileserversuit.quiz.model;

public abstract class Question {

    public static final int TYPE_SINGLE_CHOICE = 1;
    public static final int TYPE_MULTIPLE_CHOICE = 2;
    public static final int TYPE_FILL_BLANK = 3;
    public static final int TYPE_MATCHING = 4;
    public static final int TYPE_ORDERING = 5;
    public static final int TYPE_VOICE = 6;
    public static final int TYPE_TRANSLATION = 7;
    public static final int TYPE_SHORT_ANSWER = 8;
    public static final int TYPE_READING = 9;

    private final int type;
    private final String stem;
    private int number;

    protected Question(int type, String stem) {
        this.type = type;
        this.stem = stem;
    }

    public int getType() {
        return type;
    }

    public String getStem() {
        return stem;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
