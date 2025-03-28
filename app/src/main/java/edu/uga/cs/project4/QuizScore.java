package edu.uga.cs.project4;

public class QuizScore {

    private long id;
    private String date;
    private int score;

    public QuizScore(String date, int score) {
        this.date = date;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

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