package com.hacaller.qualificator;

// Hey !! I will use polymorphism
public class CourseMetricModel {

    float score;
    float weight;
    float average;

    public float getScore() {
        return score;
    }

    public String getScoreAsString() {
        return String.valueOf(score);
    }

    public void setScore(String score) {
        this.score = Float.parseFloat(score);
        this.average = (this.score * this.weight) / 100f;
    }

    public void setScore(float score) {
        this.score = score;
        this.average = (this.score * this.weight) / 100f;
    }

    public void setWeight(float weight) {
        this.weight = weight;
        this.average = (this.score * this.weight) / 100f;
    }

    public float getWeight() {
        return weight;
    }

    public String getWeightAsString() {
        return String.valueOf(weight);
    }

    public void setWeight(String weight) {
        this.weight = Float.parseFloat(weight);
        this.average = (this.score * this.weight) / 100f;
    }

    public CourseMetricModel(String weight, String score) {
        this.score = Float.parseFloat(score);
        this.weight = Float.parseFloat(weight);
        this.average = (this.score * this.weight) / 100f;
    }

    public float getAverage() {
        return average;
    }
}
