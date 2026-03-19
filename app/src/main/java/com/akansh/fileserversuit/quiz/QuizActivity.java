package com.akansh.fileserversuit.quiz;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akansh.fileserversuit.R;
import com.akansh.fileserversuit.quiz.model.FillBlankQuestion;
import com.akansh.fileserversuit.quiz.model.MatchingQuestion;
import com.akansh.fileserversuit.quiz.model.MultipleChoiceQuestion;
import com.akansh.fileserversuit.quiz.model.OrderingQuestion;
import com.akansh.fileserversuit.quiz.model.Question;
import com.akansh.fileserversuit.quiz.model.ReadingQuestion;
import com.akansh.fileserversuit.quiz.model.ShortAnswerQuestion;
import com.akansh.fileserversuit.quiz.model.SingleChoiceQuestion;
import com.akansh.fileserversuit.quiz.model.TranslationQuestion;
import com.akansh.fileserversuit.quiz.model.VoiceQuestion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String RECORDING_FILE_EXTENSION = ".3gp";
    private static final int RECORDING_OUTPUT_FORMAT = MediaRecorder.OutputFormat.THREE_GPP;
    private static final int RECORDING_AUDIO_ENCODER = MediaRecorder.AudioEncoder.AMR_NB;

    private RecyclerView rvQuestions;
    private TextView tvProgress;
    private QuizAdapter quizAdapter;
    private List<Question> questions;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String currentRecordingPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        rvQuestions = findViewById(R.id.rv_questions);
        tvProgress = findViewById(R.id.tv_progress);
        ImageButton btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        questions = buildSampleQuestions();
        tvProgress.setText(questions.size() + " questions");

        quizAdapter = new QuizAdapter(this, questions);
        quizAdapter.setVoiceActionListener(new QuizAdapter.VoiceActionListener() {
            @Override
            public void onRecordToggle(int pos, VoiceQuestion question) {
                handleRecordToggle(pos, question);
            }

            @Override
            public void onPlayRecording(int pos, VoiceQuestion question) {
                handlePlayRecording(question);
            }

            @Override
            public void onDeleteRecording(int pos, VoiceQuestion question) {
                handleDeleteRecording(pos, question);
            }
        });

        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        rvQuestions.setAdapter(quizAdapter);

        findViewById(R.id.btn_submit).setOnClickListener(v -> showSubmitDialog());
    }

    private List<Question> buildSampleQuestions() {
        List<Question> list = new ArrayList<>();

        // a. Single choice
        list.add(new SingleChoiceQuestion(
                "Which of the following is the capital of France?",
                Arrays.asList("Berlin", "Madrid", "Paris", "Rome")));

        // b. Multiple choice
        list.add(new MultipleChoiceQuestion(
                "Which of the following are programming languages?",
                Arrays.asList("Java", "HTML", "Python", "CSS", "Kotlin")));

        // c. Fill in the blank
        list.add(new FillBlankQuestion(
                "Complete the sentence with appropriate words.",
                "The _____ (1) is the largest planet in our solar system, and it has _____ (2) known moons.",
                2));

        // d. Matching
        list.add(new MatchingQuestion(
                "Match each country with its capital city.",
                Arrays.asList("France", "Japan", "Brazil", "Australia"),
                Arrays.asList("Canberra", "Paris", "Tokyo", "Brasília")));

        // e. Ordering
        list.add(new OrderingQuestion(
                "Arrange the following steps of the water cycle in the correct order.",
                Arrays.asList("Evaporation", "Condensation", "Precipitation", "Collection")));

        // f. Voice
        list.add(new VoiceQuestion(
                "Listen and record your answer in English.",
                "Describe your daily routine in 3–5 sentences."));

        // g. Translation
        list.add(new TranslationQuestion(
                "Translate the following sentence into English.",
                "Le soleil se lève à l'est et se couche à l'ouest.",
                "French",
                "English"));

        // h. Short answer
        list.add(new ShortAnswerQuestion(
                "Explain in your own words the significance of the Industrial Revolution."));

        // i. Reading comprehension
        List<Question> subQuestions = new ArrayList<>();
        subQuestions.add(new SingleChoiceQuestion(
                "According to the passage, what is the main theme?",
                Arrays.asList("Technology", "Nature", "History", "Science")));
        subQuestions.add(new MultipleChoiceQuestion(
                "Which details are mentioned in the passage?",
                Arrays.asList("Forests", "Oceans", "Mountains", "Deserts")));
        subQuestions.add(new FillBlankQuestion(
                "Complete based on the passage.",
                "The article states that _____ (1) covers about 71% of the Earth's surface.",
                1));
        subQuestions.add(new ShortAnswerQuestion(
                "What is the author's opinion about environmental conservation?"));
        list.add(new ReadingQuestion(
                "Read the passage and answer the questions below.",
                "Our Changing Planet",
                "Earth is the only known planet to support life. It is often called the \"Blue Planet\" " +
                        "because water covers approximately 71% of its surface. The planet is home to a " +
                        "vast diversity of ecosystems ranging from dense tropical forests and expansive " +
                        "oceans to arid deserts and towering mountains. In recent decades, human activity " +
                        "has significantly altered these ecosystems, prompting scientists and policymakers " +
                        "alike to call for urgent environmental action. Protecting our natural world is not " +
                        "only a moral responsibility but also essential for the survival of all species, " +
                        "including humanity itself.",
                subQuestions));

        return list;
    }

    private void handleRecordToggle(int pos, VoiceQuestion question) {
        if (question.isRecording()) {
            stopRecording(question);
        } else {
            startRecording(question);
        }
        quizAdapter.notifyItemChanged(pos);
    }

    private void startRecording(VoiceQuestion question) {
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
        currentRecordingPath = new File(getCacheDir(),
                "voice_" + System.currentTimeMillis() + RECORDING_FILE_EXTENSION).getAbsolutePath();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(RECORDING_OUTPUT_FORMAT);
        mediaRecorder.setAudioEncoder(RECORDING_AUDIO_ENCODER);
        mediaRecorder.setOutputFile(currentRecordingPath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            question.setRecording(true);
        } catch (IOException e) {
            Log.e(TAG, "Failed to start recording", e);
            Toast.makeText(this, "Failed to start recording", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording(VoiceQuestion question) {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                // stop() can throw if recording never started (e.g. permission denied at runtime).
                // In this case, discard the incomplete file path.
                Log.w(TAG, "MediaRecorder.stop() failed — recording discarded", e);
                currentRecordingPath = null;
            }
            mediaRecorder.release();
            mediaRecorder = null;
        }
        question.setRecording(false);
        question.setRecordedFilePath(currentRecordingPath);
    }

    private void handlePlayRecording(VoiceQuestion question) {
        if (question.getRecordedFilePath() == null) return;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(question.getRecordedFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> mp.release());
        } catch (IOException e) {
            Toast.makeText(this, "Failed to play recording", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleDeleteRecording(int pos, VoiceQuestion question) {
        if (question.getRecordedFilePath() != null) {
            new File(question.getRecordedFilePath()).delete();
        }
        question.setRecordedFilePath(null);
        quizAdapter.notifyItemChanged(pos);
    }

    private void showSubmitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Submit Quiz")
                .setMessage("Are you sure you want to submit your answers?")
                .setPositiveButton("Submit", (dialog, which) ->
                        Toast.makeText(this, "Quiz submitted!", Toast.LENGTH_SHORT).show())
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
