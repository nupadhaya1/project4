package edu.uga.cs.project4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.List;

public class QuestionFragment extends Fragment {

    // Keys for passing arguments to this fragment
    private static final String ARG_QUESTION = "question";
    private static final String ARG_POSITION = "position";

    // The question to display
    private Question question;
    private int position;

    // UI elements
    private RadioGroup answersGroup;
    private QuizPagerAdapter.AnswerSelectedListener listener;

    /**
     * Factory method to create a new instance of this fragment with the question,
     * its position, and a listener for answer selection.
     */
    public static QuestionFragment newInstance(Question question, int position,
                                               QuizPagerAdapter.AnswerSelectedListener listener) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();

        // Pass the question
        args.putSerializable(ARG_QUESTION, question);

        // Pass the position
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);

        // Set the callback listener
        fragment.setListener(listener);
        return fragment;
    }

    // Setter method for the answer listener (used internally in newInstance)
    public void setListener(QuizPagerAdapter.AnswerSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * Called when the fragment is first created
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().getSerializable(ARG_QUESTION);
            position = getArguments().getInt(ARG_POSITION);
        } // if statement
    } // onCreate

    /**
     * Creates and returns the fragment's view. Initializes and populates the UI.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        // Get references to UI elements
        TextView questionText = view.findViewById(R.id.questionText);
        TextView questionCountText = view.findViewById(R.id.questionCountText);
        answersGroup = view.findViewById(R.id.answersGroup);
        RadioButton answerA = view.findViewById(R.id.answerA);
        RadioButton answerB = view.findViewById(R.id.answerB);
        RadioButton answerC = view.findViewById(R.id.answerC);

        // Set the question and answer text
        List<String> options = question.getAnswerOptions();
        questionText.setText("Which continent is " + question.getCountryName() + " located in?");
        questionCountText.setText("Quiz Progress: " + (position + 1) + "/6");

        answerA.setText("A. " + options.get(0));
        answerB.setText("B. " + options.get(1));
        answerC.setText("C. " + options.get(2));

        // When a user selects an answer, notify the adapter through the listener
        answersGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = group.findViewById(checkedId);
            if (selected != null && listener != null) {
                // Just the continent name
                String selectedContinent = selected.getText().toString().substring(3);
                listener.onAnswerSelected(position, selectedContinent);

                // If this is the last question (position 5), trigger the finish callback
                if (position == 5) {
                    listener.onLastQuestionAnswered();
                } // if statement
            } // if stataement
        }); // listener

        return view;
    } // onCreateView

    /**
     * Helper method to get the currently selected answer for this question.
     */
    public String getSelectedAnswer() {
        int selectedId = answersGroup.getCheckedRadioButtonId();
        if (selectedId == -1) return null;
        RadioButton selected = getView().findViewById(selectedId);
        return selected.getText().toString().substring(3); // remove "A. "
    } // getSelectedAnswer

} // QuestionFragment

