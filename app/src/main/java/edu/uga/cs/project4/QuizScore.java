package edu.uga.cs.project4;

public class QuizScore {

    // Unique identifier for the quiz score
    private long id;

    // Date the quiz was taken
    private String date;

    // The score the user received
    private int score;

    // Constructor to create a QuizScore object with date and score
    public QuizScore(String date, int score) {
        this.date = date;
        this.score = score;
    }

    // Getter for ID
    public long getId() {
        return id;
    }

    // Setter for ID
    public void setId(long id) {
        this.id = id;
    }

    // Getter for data
    public String getDate() {
        return date;
    }

    // Setter for data
    public void setDate(String date) {
        this.date = date;
    }

    // Getter for score
    public int getScore() {
        return score;
    }

    // Setter Score
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "QuizScore{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", score=" + score +
                '}';
    }
}