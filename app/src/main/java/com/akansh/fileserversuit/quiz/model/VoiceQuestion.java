package com.akansh.fileserversuit.quiz.model;

public class VoiceQuestion extends Question {

    private final String audioPrompt;
    private String recordedFilePath;
    private boolean isRecording = false;

    public VoiceQuestion(String stem, String audioPrompt) {
        super(TYPE_VOICE, stem);
        this.audioPrompt = audioPrompt;
    }

    public String getAudioPrompt() {
        return audioPrompt;
    }

    public String getRecordedFilePath() {
        return recordedFilePath;
    }

    public void setRecordedFilePath(String recordedFilePath) {
        this.recordedFilePath = recordedFilePath;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public boolean hasRecording() {
        return recordedFilePath != null && !recordedFilePath.isEmpty();
    }
}
