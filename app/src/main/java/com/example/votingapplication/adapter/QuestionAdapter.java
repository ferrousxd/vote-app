package com.example.votingapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingapplication.R;
import com.example.votingapplication.model.Option;
import com.example.votingapplication.model.Question;
import com.example.votingapplication.model.Vote;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.R)
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Question> questions;
    private final List<Vote> votes;

    public QuestionAdapter(Context context, List<Question> questions, List<Vote> votes) {
        this.inflater = LayoutInflater.from(context);
        this.questions = questions;
        this.votes = votes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.question_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question currentQuestion = questions.get(position);

        holder.bind(currentQuestion);

        boolean isAnswered = false;
        for (Vote vote : votes) {
            if (vote.getQuestionUid().equals(currentQuestion.getUid())) {
                isAnswered = true;
                break;
            }
        }

        // Достаем views из view holder
        TextView questionTitleTextView = holder.questionTitleTextView;
        RadioGroup optionsRadioGroup = holder.optionsRadioGroup;
        Button submitButton = holder.submitButton;

        if (isAnswered) {
            submitButton.setEnabled(false);
        }

        // Название вопроса
        questionTitleTextView.setText(currentQuestion.getTitle());

        for (Option option : currentQuestion.getOptions()) {
            RadioButton optionRadioButton = new RadioButton(inflater.getContext());
            optionRadioButton.setId(View.generateViewId());
            optionRadioButton.setText(option.getTitle());

            if (isAnswered) {
                optionRadioButton.setEnabled(false);
            }

            optionsRadioGroup.addView(optionRadioButton);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView questionTitleTextView;
        private final RadioGroup optionsRadioGroup;
        private final Button submitButton;
        private final FirebaseFirestore db;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTitleTextView = itemView.findViewById(R.id.question_title);
            optionsRadioGroup = itemView.findViewById(R.id.options_radio_group);
            submitButton = itemView.findViewById(R.id.submit_button);
            db = FirebaseFirestore.getInstance();
        }

        public void bind(Question question) {
            submitButton.setOnClickListener(v -> {
                int selectedId = optionsRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = itemView.findViewById(selectedId);
                int optionIndex = optionsRadioGroup.indexOfChild(selectedRadioButton);

                Map<String, Object> data = new HashMap<>();
                data.put("userUID", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                data.put("questionUID", question.getUid());
                data.put("optionUID", question.getOptions().get(optionIndex).getUid());

                db.collection("votes")
                        .add(data)
                        .addOnCompleteListener(task -> {
                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Success!")
                                    .setMessage("Your vote was successfully submitted!")
                                    .setCancelable(true)
                                    .show();
                            submitButton.setEnabled(false);
                            for (int i = 0; i < optionsRadioGroup.getChildCount(); i++) {
                                optionsRadioGroup.getChildAt(i).setEnabled(false);
                            }
                            optionsRadioGroup.clearCheck();
                        });
            });
        }
    }
}
