package edu.uga.cs.project4;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

/**
 * A ViewPager2 adapter that manages the swipeable quiz questions.
 */
public class QuizPagerAdapter extends FragmentStateAdapter {

    /**
     * Listener interface to handle answer selection and quiz completion.
     */
    public interface AnswerSelectedListener {
        // Called when a user selects an answer for a question
        void onAnswerSelected(int position, String answer);

        // Called only when the last question is answered
        void onLastQuestionAnswered();
    }

    // List of questions for the quiz
    private final List<Question> questions;

    // Callback listener for when an answer is selected
    private final AnswerSelectedListener listener;

    /**
     * Constructor for the adapter.
     */
    public QuizPagerAdapter(@NonNull FragmentActivity activity,
                            List<Question> questions,
                            AnswerSelectedListener listener) {
        super(activity);
        this.questions = questions;
        this.listener = listener;
    }

    /**
     * Creates and returns a fragment for the specified position.
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return QuestionFragment.newInstance(questions.get(position), position, listener);
    }

    /**
     * Returns the total number of quiz questions.
     */
    @Override
    public int getItemCount() {
        return questions.size();
    }
}
