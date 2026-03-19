package com.akansh.fileserversuit.quiz;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<Question> questions;
    private VoiceActionListener voiceActionListener;

    public interface VoiceActionListener {
        void onRecordToggle(int questionPosition, VoiceQuestion question);
        void onPlayRecording(int questionPosition, VoiceQuestion question);
        void onDeleteRecording(int questionPosition, VoiceQuestion question);
    }

    public QuizAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    public void setVoiceActionListener(VoiceActionListener listener) {
        this.voiceActionListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return questions.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case Question.TYPE_SINGLE_CHOICE:
                return new SingleChoiceVH(inflater.inflate(
                        R.layout.question_item_single_choice, parent, false));
            case Question.TYPE_MULTIPLE_CHOICE:
                return new MultipleChoiceVH(inflater.inflate(
                        R.layout.question_item_multiple_choice, parent, false));
            case Question.TYPE_FILL_BLANK:
                return new FillBlankVH(inflater.inflate(
                        R.layout.question_item_fill_blank, parent, false));
            case Question.TYPE_MATCHING:
                return new MatchingVH(inflater.inflate(
                        R.layout.question_item_matching, parent, false));
            case Question.TYPE_ORDERING:
                return new OrderingVH(inflater.inflate(
                        R.layout.question_item_ordering, parent, false));
            case Question.TYPE_VOICE:
                return new VoiceVH(inflater.inflate(
                        R.layout.question_item_voice, parent, false));
            case Question.TYPE_TRANSLATION:
                return new TranslationVH(inflater.inflate(
                        R.layout.question_item_translation, parent, false));
            case Question.TYPE_SHORT_ANSWER:
                return new ShortAnswerVH(inflater.inflate(
                        R.layout.question_item_short_answer, parent, false));
            case Question.TYPE_READING:
                return new ReadingVH(inflater.inflate(
                        R.layout.question_item_reading, parent, false));
            default:
                throw new IllegalStateException("Unknown question type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Question question = questions.get(position);
        switch (question.getType()) {
            case Question.TYPE_SINGLE_CHOICE:
                bindSingleChoice((SingleChoiceVH) holder, (SingleChoiceQuestion) question, position);
                break;
            case Question.TYPE_MULTIPLE_CHOICE:
                bindMultipleChoice((MultipleChoiceVH) holder, (MultipleChoiceQuestion) question, position);
                break;
            case Question.TYPE_FILL_BLANK:
                bindFillBlank((FillBlankVH) holder, (FillBlankQuestion) question, position);
                break;
            case Question.TYPE_MATCHING:
                bindMatching((MatchingVH) holder, (MatchingQuestion) question, position);
                break;
            case Question.TYPE_ORDERING:
                bindOrdering((OrderingVH) holder, (OrderingQuestion) question, position);
                break;
            case Question.TYPE_VOICE:
                bindVoice((VoiceVH) holder, (VoiceQuestion) question, position);
                break;
            case Question.TYPE_TRANSLATION:
                bindTranslation((TranslationVH) holder, (TranslationQuestion) question, position);
                break;
            case Question.TYPE_SHORT_ANSWER:
                bindShortAnswer((ShortAnswerVH) holder, (ShortAnswerQuestion) question, position);
                break;
            case Question.TYPE_READING:
                bindReading((ReadingVH) holder, (ReadingQuestion) question, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    // -----------------------------------------------------------------------
    // Single Choice
    // -----------------------------------------------------------------------

    private void bindSingleChoice(SingleChoiceVH vh, SingleChoiceQuestion q, int pos) {
        vh.tvNumber.setText(context.getString(R.string.quiz_question_label, pos + 1));
        vh.tvStem.setText(q.getStem());
        vh.rgOptions.removeAllViews();
        vh.rgOptions.clearCheck();

        for (int i = 0; i < q.getOptions().size(); i++) {
            RadioButton rb = (RadioButton) LayoutInflater.from(context)
                    .inflate(R.layout.item_radio_option, vh.rgOptions, false);
            rb.setText(q.getOptions().get(i));
            rb.setId(View.generateViewId());
            if (q.getSelectedIndex() == i) rb.setChecked(true);
            final int index = i;
            rb.setOnCheckedChangeListener((btn, isChecked) -> {
                if (isChecked) q.setSelectedIndex(index);
            });
            vh.rgOptions.addView(rb);
        }
    }

    // -----------------------------------------------------------------------
    // Multiple Choice
    // -----------------------------------------------------------------------

    private void bindMultipleChoice(MultipleChoiceVH vh, MultipleChoiceQuestion q, int pos) {
        vh.tvNumber.setText(context.getString(R.string.quiz_question_label, pos + 1));
        vh.tvStem.setText(q.getStem());
        vh.llOptions.removeAllViews();

        for (int i = 0; i < q.getOptions().size(); i++) {
            CheckBox cb = (CheckBox) LayoutInflater.from(context)
                    .inflate(R.layout.item_checkbox_option, vh.llOptions, false);
            cb.setText(q.getOptions().get(i));
            cb.setChecked(q.isSelected(i));
            final int index = i;
            cb.setOnCheckedChangeListener((btn, isChecked) -> {
                if (isChecked != q.isSelected(index)) {
                    q.toggleSelection(index);
                }
            });
            vh.llOptions.addView(cb);
        }
    }

    // -----------------------------------------------------------------------
    // Fill in the Blank
    // -----------------------------------------------------------------------

    private void bindFillBlank(FillBlankVH vh, FillBlankQuestion q, int pos) {
        vh.tvNumber.setText(context.getString(R.string.quiz_question_label, pos + 1));
        vh.tvStem.setText(q.getStem());
        vh.tvTextWithBlanks.setText(q.getTextWithBlanks());
        vh.llBlanks.removeAllViews();

        for (int i = 0; i < q.getBlankCount(); i++) {
            View blankView = LayoutInflater.from(context)
                    .inflate(R.layout.item_fill_blank, vh.llBlanks, false);
            TextView label = blankView.findViewById(R.id.tv_blank_label);
            EditText et = blankView.findViewById(R.id.et_blank_answer);
            label.setText("(" + (i + 1) + ")");
            et.setText(q.getAnswers().get(i));
            final int index = i;
            et.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    q.setAnswer(index, s.toString());
                }
            });
            vh.llBlanks.addView(blankView);
        }
    }

    // -----------------------------------------------------------------------
    // Matching
    // -----------------------------------------------------------------------

    private void bindMatching(MatchingVH vh, MatchingQuestion q, int pos) {
        vh.tvNumber.setText(context.getString(R.string.quiz_question_label, pos + 1));
        vh.tvStem.setText(q.getStem());

        vh.llLeft.removeAllViews();
        vh.llRight.removeAllViews();

        final int[] selectedLeft = {-1};

        for (int i = 0; i < q.getLeftItems().size(); i++) {
            TextView tv = (TextView) LayoutInflater.from(context)
                    .inflate(R.layout.item_matching_cell, vh.llLeft, false);
            tv.setText(q.getLeftItems().get(i));
            final int leftIdx = i;
            tv.setOnClickListener(v -> {
                selectedLeft[0] = leftIdx;
                updateMatchingHighlight(vh.llLeft, leftIdx);
            });
            if (q.getMatchedRight(i) >= 0) tv.setActivated(true);
            vh.llLeft.addView(tv);
        }

        for (int j = 0; j < q.getRightItems().size(); j++) {
            TextView tv = (TextView) LayoutInflater.from(context)
                    .inflate(R.layout.item_matching_cell, vh.llRight, false);
            tv.setText(q.getRightItems().get(j));
            final int rightIdx = j;
            tv.setOnClickListener(v -> {
                if (selectedLeft[0] >= 0) {
                    q.setMatch(selectedLeft[0], rightIdx);
                    ((TextView) vh.llLeft.getChildAt(selectedLeft[0])).setActivated(true);
                    tv.setActivated(true);
                    selectedLeft[0] = -1;
                    clearMatchingHighlight(vh.llLeft);
                }
            });
            vh.llRight.addView(tv);
        }
    }

    private void updateMatchingHighlight(LinearLayout container, int selectedIndex) {
        for (int i = 0; i < container.getChildCount(); i++) {
            container.getChildAt(i).setSelected(i == selectedIndex);
        }
    }

    private void clearMatchingHighlight(LinearLayout container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            container.getChildAt(i).setSelected(false);
        }
    }

    // -----------------------------------------------------------------------
    // Ordering
    // -----------------------------------------------------------------------

    private void bindOrdering(OrderingVH vh, OrderingQuestion q, int pos) {
        vh.tvNumber.setText(context.getString(R.string.quiz_question_label, pos + 1));
        vh.tvStem.setText(q.getStem());

        OrderingItemAdapter orderAdapter = new OrderingItemAdapter(q);
        vh.rvItems.setLayoutManager(new LinearLayoutManager(context));
        vh.rvItems.setAdapter(orderAdapter);

        OrderingItemTouchCallback callback = new OrderingItemTouchCallback(orderAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(vh.rvItems);
    }

    // -----------------------------------------------------------------------
    // Voice
    // -----------------------------------------------------------------------

    private void bindVoice(VoiceVH vh, VoiceQuestion q, int pos) {
        vh.tvNumber.setText(context.getString(R.string.quiz_question_label, pos + 1));
        vh.tvStem.setText(q.getStem());
        vh.tvPrompt.setText(q.getAudioPrompt());
        updateVoiceUI(vh, q);

        vh.btnRecord.setOnClickListener(v -> {
            if (voiceActionListener != null) {
                voiceActionListener.onRecordToggle(pos, q);
            }
        });
        vh.btnPlay.setOnClickListener(v -> {
            if (voiceActionListener != null) {
                voiceActionListener.onPlayRecording(pos, q);
            }
        });
        vh.btnDeleteRecording.setOnClickListener(v -> {
            if (voiceActionListener != null) {
                voiceActionListener.onDeleteRecording(pos, q);
            }
        });
    }

    private void updateVoiceUI(VoiceVH vh, VoiceQuestion q) {
        if (q.isRecording()) {
            vh.tvStatus.setText(context.getString(R.string.quiz_tap_to_record));
            vh.tvDuration.setVisibility(View.VISIBLE);
        } else {
            vh.tvStatus.setText(context.getString(R.string.quiz_tap_to_record));
            vh.tvDuration.setVisibility(View.GONE);
        }
        vh.llPlayback.setVisibility(q.hasRecording() ? View.VISIBLE : View.GONE);
    }

    // -----------------------------------------------------------------------
    // Translation
    // -----------------------------------------------------------------------

    private void bindTranslation(TranslationVH vh, TranslationQuestion q, int pos) {
        vh.tvNumber.setText(context.getString(R.string.quiz_question_label, pos + 1));
        vh.tvStem.setText(q.getStem());
        vh.tvSourceLang.setText(q.getSourceLanguage());
        vh.tvTargetLang.setText(q.getTargetLanguage());
        vh.tvSourceText.setText(q.getSourceText());
        if (vh.textWatcher != null) {
            vh.etTranslation.removeTextChangedListener(vh.textWatcher);
        }
        vh.etTranslation.setText(q.getUserTranslation());
        vh.textWatcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                q.setUserTranslation(s.toString());
            }
        };
        vh.etTranslation.addTextChangedListener(vh.textWatcher);
    }

    // -----------------------------------------------------------------------
    // Short Answer
    // -----------------------------------------------------------------------

    private void bindShortAnswer(ShortAnswerVH vh, ShortAnswerQuestion q, int pos) {
        vh.tvNumber.setText(context.getString(R.string.quiz_question_label, pos + 1));
        vh.tvStem.setText(q.getStem());
        if (vh.textWatcher != null) {
            vh.etAnswer.removeTextChangedListener(vh.textWatcher);
        }
        vh.etAnswer.setText(q.getUserAnswer());
        vh.tvCharCount.setText(String.valueOf(q.getUserAnswer().length()));
        vh.textWatcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                q.setUserAnswer(s.toString());
                vh.tvCharCount.setText(String.valueOf(s.length()));
            }
        };
        vh.etAnswer.addTextChangedListener(vh.textWatcher);
    }

    // -----------------------------------------------------------------------
    // Reading Comprehension
    // -----------------------------------------------------------------------

    private void bindReading(ReadingVH vh, ReadingQuestion q, int pos) {
        vh.tvNumber.setText(context.getString(R.string.quiz_question_label, pos + 1));
        vh.tvTitle.setText(q.getArticleTitle());
        vh.tvContent.setText(q.getArticleContent());
        vh.llSubQuestions.removeAllViews();

        for (int i = 0; i < q.getSubQuestions().size(); i++) {
            Question subQ = q.getSubQuestions().get(i);
            View subView = buildSubQuestionView(subQ, i, vh.llSubQuestions);
            if (subView != null) {
                vh.llSubQuestions.addView(subView);
            }
        }
    }

    private View buildSubQuestionView(Question subQ, int subIndex, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (subQ.getType()) {
            case Question.TYPE_SINGLE_CHOICE: {
                View v = inflater.inflate(R.layout.question_item_single_choice, parent, false);
                bindSingleChoiceView(v, (SingleChoiceQuestion) subQ, subIndex);
                return v;
            }
            case Question.TYPE_MULTIPLE_CHOICE: {
                View v = inflater.inflate(R.layout.question_item_multiple_choice, parent, false);
                bindMultipleChoiceView(v, (MultipleChoiceQuestion) subQ, subIndex);
                return v;
            }
            case Question.TYPE_FILL_BLANK: {
                View v = inflater.inflate(R.layout.question_item_fill_blank, parent, false);
                bindFillBlankView(v, (FillBlankQuestion) subQ, subIndex);
                return v;
            }
            case Question.TYPE_SHORT_ANSWER: {
                View v = inflater.inflate(R.layout.question_item_short_answer, parent, false);
                bindShortAnswerView(v, (ShortAnswerQuestion) subQ, subIndex);
                return v;
            }
            default:
                return null;
        }
    }

    private void bindSingleChoiceView(View v, SingleChoiceQuestion q, int subIndex) {
        TextView tvNum = v.findViewById(R.id.tv_question_number);
        TextView tvStem = v.findViewById(R.id.tv_stem);
        RadioGroup rg = v.findViewById(R.id.rg_options);
        tvNum.setText(context.getString(R.string.quiz_question_label, subIndex + 1));
        tvStem.setText(q.getStem());
        rg.removeAllViews();
        for (int i = 0; i < q.getOptions().size(); i++) {
            RadioButton rb = (RadioButton) LayoutInflater.from(context)
                    .inflate(R.layout.item_radio_option, rg, false);
            rb.setText(q.getOptions().get(i));
            rb.setId(View.generateViewId());
            if (q.getSelectedIndex() == i) rb.setChecked(true);
            final int idx = i;
            rb.setOnCheckedChangeListener((btn, checked) -> { if (checked) q.setSelectedIndex(idx); });
            rg.addView(rb);
        }
    }

    private void bindMultipleChoiceView(View v, MultipleChoiceQuestion q, int subIndex) {
        TextView tvNum = v.findViewById(R.id.tv_question_number);
        TextView tvStem = v.findViewById(R.id.tv_stem);
        LinearLayout ll = v.findViewById(R.id.ll_options);
        tvNum.setText(context.getString(R.string.quiz_question_label, subIndex + 1));
        tvStem.setText(q.getStem());
        ll.removeAllViews();
        for (int i = 0; i < q.getOptions().size(); i++) {
            CheckBox cb = (CheckBox) LayoutInflater.from(context)
                    .inflate(R.layout.item_checkbox_option, ll, false);
            cb.setText(q.getOptions().get(i));
            cb.setChecked(q.isSelected(i));
            final int idx = i;
            cb.setOnCheckedChangeListener((btn, checked) -> { if (checked != q.isSelected(idx)) q.toggleSelection(idx); });
            ll.addView(cb);
        }
    }

    private void bindFillBlankView(View v, FillBlankQuestion q, int subIndex) {
        TextView tvNum = v.findViewById(R.id.tv_question_number);
        TextView tvStem = v.findViewById(R.id.tv_stem);
        TextView tvText = v.findViewById(R.id.tv_text_with_blanks);
        LinearLayout ll = v.findViewById(R.id.ll_blanks);
        tvNum.setText(context.getString(R.string.quiz_question_label, subIndex + 1));
        tvStem.setText(q.getStem());
        tvText.setText(q.getTextWithBlanks());
        ll.removeAllViews();
        for (int i = 0; i < q.getBlankCount(); i++) {
            View blankView = LayoutInflater.from(context).inflate(R.layout.item_fill_blank, ll, false);
            TextView label = blankView.findViewById(R.id.tv_blank_label);
            EditText et = blankView.findViewById(R.id.et_blank_answer);
            label.setText("(" + (i + 1) + ")");
            et.setText(q.getAnswers().get(i));
            final int idx = i;
            et.addTextChangedListener(new SimpleTextWatcher() {
                @Override public void afterTextChanged(Editable s) { q.setAnswer(idx, s.toString()); }
            });
            ll.addView(blankView);
        }
    }

    private void bindShortAnswerView(View v, ShortAnswerQuestion q, int subIndex) {
        TextView tvNum = v.findViewById(R.id.tv_question_number);
        TextView tvStem = v.findViewById(R.id.tv_stem);
        EditText et = v.findViewById(R.id.et_answer);
        TextView tvChar = v.findViewById(R.id.tv_char_count);
        tvNum.setText(context.getString(R.string.quiz_question_label, subIndex + 1));
        tvStem.setText(q.getStem());
        et.setText(q.getUserAnswer());
        et.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                q.setUserAnswer(s.toString());
                tvChar.setText(String.valueOf(s.length()));
            }
        });
    }

    // -----------------------------------------------------------------------
    // ViewHolders
    // -----------------------------------------------------------------------

    static class SingleChoiceVH extends RecyclerView.ViewHolder {
        final TextView tvNumber, tvStem;
        final RadioGroup rgOptions;

        SingleChoiceVH(@NonNull View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tv_question_number);
            tvStem = v.findViewById(R.id.tv_stem);
            rgOptions = v.findViewById(R.id.rg_options);
        }
    }

    static class MultipleChoiceVH extends RecyclerView.ViewHolder {
        final TextView tvNumber, tvStem;
        final LinearLayout llOptions;

        MultipleChoiceVH(@NonNull View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tv_question_number);
            tvStem = v.findViewById(R.id.tv_stem);
            llOptions = v.findViewById(R.id.ll_options);
        }
    }

    static class FillBlankVH extends RecyclerView.ViewHolder {
        final TextView tvNumber, tvStem, tvTextWithBlanks;
        final LinearLayout llBlanks;

        FillBlankVH(@NonNull View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tv_question_number);
            tvStem = v.findViewById(R.id.tv_stem);
            tvTextWithBlanks = v.findViewById(R.id.tv_text_with_blanks);
            llBlanks = v.findViewById(R.id.ll_blanks);
        }
    }

    static class MatchingVH extends RecyclerView.ViewHolder {
        final TextView tvNumber, tvStem;
        final LinearLayout llLeft, llRight;

        MatchingVH(@NonNull View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tv_question_number);
            tvStem = v.findViewById(R.id.tv_stem);
            llLeft = v.findViewById(R.id.ll_left_items);
            llRight = v.findViewById(R.id.ll_right_items);
        }
    }

    static class OrderingVH extends RecyclerView.ViewHolder {
        final TextView tvNumber, tvStem;
        final RecyclerView rvItems;

        OrderingVH(@NonNull View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tv_question_number);
            tvStem = v.findViewById(R.id.tv_stem);
            rvItems = v.findViewById(R.id.rv_ordering_items);
        }
    }

    static class VoiceVH extends RecyclerView.ViewHolder {
        final TextView tvNumber, tvStem, tvPrompt, tvStatus, tvDuration;
        final View btnRecord, btnPlay, btnDeleteRecording;
        final LinearLayout llPlayback;

        VoiceVH(@NonNull View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tv_question_number);
            tvStem = v.findViewById(R.id.tv_stem);
            tvPrompt = v.findViewById(R.id.tv_audio_prompt);
            tvStatus = v.findViewById(R.id.tv_recording_status);
            tvDuration = v.findViewById(R.id.tv_recording_duration);
            btnRecord = v.findViewById(R.id.btn_record);
            btnPlay = v.findViewById(R.id.btn_play);
            btnDeleteRecording = v.findViewById(R.id.btn_delete_recording);
            llPlayback = v.findViewById(R.id.ll_playback);
        }
    }

    static class TranslationVH extends RecyclerView.ViewHolder {
        final TextView tvNumber, tvStem, tvSourceLang, tvTargetLang, tvSourceText;
        final EditText etTranslation;
        TextWatcher textWatcher;

        TranslationVH(@NonNull View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tv_question_number);
            tvStem = v.findViewById(R.id.tv_stem);
            tvSourceLang = v.findViewById(R.id.tv_source_lang_label);
            tvTargetLang = v.findViewById(R.id.tv_target_lang_label);
            tvSourceText = v.findViewById(R.id.tv_source_text);
            etTranslation = v.findViewById(R.id.et_translation);
        }
    }

    static class ShortAnswerVH extends RecyclerView.ViewHolder {
        final TextView tvNumber, tvStem, tvCharCount;
        final EditText etAnswer;
        TextWatcher textWatcher;

        ShortAnswerVH(@NonNull View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tv_question_number);
            tvStem = v.findViewById(R.id.tv_stem);
            etAnswer = v.findViewById(R.id.et_answer);
            tvCharCount = v.findViewById(R.id.tv_char_count);
        }
    }

    static class ReadingVH extends RecyclerView.ViewHolder {
        final TextView tvNumber, tvTitle, tvContent;
        final LinearLayout llSubQuestions;

        ReadingVH(@NonNull View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tv_question_number);
            tvTitle = v.findViewById(R.id.tv_article_title);
            tvContent = v.findViewById(R.id.tv_article_content);
            llSubQuestions = v.findViewById(R.id.ll_sub_questions);
        }
    }

    // -----------------------------------------------------------------------
    // Utilities
    // -----------------------------------------------------------------------

    abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
}
