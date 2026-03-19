package com.akansh.fileserversuit.quiz.model;

public class TranslationQuestion extends Question {

    private final String sourceText;
    private final String sourceLanguage;
    private final String targetLanguage;
    private String userTranslation = "";

    public TranslationQuestion(String stem, String sourceText, String sourceLanguage, String targetLanguage) {
        super(TYPE_TRANSLATION, stem);
        this.sourceText = sourceText;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }

    public String getSourceText() {
        return sourceText;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public String getUserTranslation() {
        return userTranslation;
    }

    public void setUserTranslation(String userTranslation) {
        this.userTranslation = userTranslation;
    }
}
