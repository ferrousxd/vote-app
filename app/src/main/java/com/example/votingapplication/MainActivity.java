package com.example.votingapplication;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingapplication.adapter.QuestionAdapter;
import com.example.votingapplication.model.Option;
import com.example.votingapplication.model.Question;
import com.example.votingapplication.model.Vote;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private final List<Question> questions = new ArrayList<>();
    private final List<Vote> votes = new ArrayList<>();

    private void fetchData() {
        List<Task<QuerySnapshot>> taskList = new ArrayList<>();

        db.collection("questions").get().addOnCompleteListener(firstTask -> {
            if (firstTask.isSuccessful()) {
                for (QueryDocumentSnapshot questionSnapshot : Objects.requireNonNull(firstTask.getResult())) {
                    List<Option> tempOptionList = new ArrayList<>();

                    Task<QuerySnapshot> fetchOptionsTask = db.collection("options")
                            .whereEqualTo("questionUID", questionSnapshot.getId())
                            .get()
                            .addOnCompleteListener(secondTask -> {
                                if (secondTask.isSuccessful()) {
                                    for (QueryDocumentSnapshot optionSnapshot : Objects.requireNonNull(secondTask.getResult())) {
                                        tempOptionList.add(new Option(
                                                optionSnapshot.getId(),
                                                (String) optionSnapshot.getData().get("title")
                                        ));
                                    }
                                }
                                questions.add(new Question(
                                        questionSnapshot.getId(),
                                        (String) questionSnapshot.getData().get("title"),
                                        tempOptionList
                                ));
                            });

                    taskList.add(fetchOptionsTask);
                }

                Task<QuerySnapshot> fetchVotesTask = db.collection("votes")
                        .whereEqualTo("userUID", Objects.requireNonNull(auth.getCurrentUser()).getUid())
                        .get()
                        .addOnCompleteListener(thirdTask -> {
                            for (QueryDocumentSnapshot voteSnapshot : Objects.requireNonNull(thirdTask.getResult())) {
                                votes.add(new Vote(
                                        voteSnapshot.getId(),
                                        (String) voteSnapshot.getData().get("userUID"),
                                        (String) voteSnapshot.getData().get("questionUID"),
                                        (String) voteSnapshot.getData().get("optionUID")
                                ));
                            }
                        });

                taskList.add(fetchVotesTask);

                Tasks.whenAllSuccess(taskList).addOnCompleteListener(task -> configureRecyclerView());
            }
        });
    }

    private void configureRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.questions);
        QuestionAdapter adapter = new QuestionAdapter(this, questions, votes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final Button button = findViewById(R.id.log_out);

        button.setOnClickListener(v -> {
            auth.signOut();
            this.finish();
        });

        fetchData();
    }

    private void testData() {
        questions.add(
                new Question("1", "Test1", List.of(
                        new Option("1", "№1"),
                        new Option("2", "№2"),
                        new Option("3", "№3"),
                        new Option("4", "№4")
                )
                ));
        questions.add(
                new Question("2", "Test2", List.of(
                        new Option("1", "№1"),
                        new Option("2", "№2"),
                        new Option("3", "№3"),
                        new Option("4", "№4")
                )
                ));
        questions.add(
                new Question("3", "Test3", List.of(
                        new Option("1", "№1"),
                        new Option("2", "№2"),
                        new Option("3", "№3"),
                        new Option("4", "№4")
                )
                ));
        questions.add(
                new Question("4", "Test4", List.of(
                        new Option("1", "№1"),
                        new Option("2", "№2"),
                        new Option("3", "№3"),
                        new Option("4", "№4")
                )
                ));
        questions.add(
                new Question("5", "Test5", List.of(
                        new Option("1", "№1"),
                        new Option("2", "№2"),
                        new Option("3", "№3"),
                        new Option("4", "№4")
                )
                ));
    }

    private void updateUI() {

    }
}