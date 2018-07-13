package com.hacaller.qualificator;

import java.util.List;

public class CourseModel {

    List<CourseMetricModel> courseMetricModels;
    String name;
    float credits;
    float overall;
    float bonus;

    public List<CourseMetricModel> getCourseMetricModels() {
        return courseMetricModels;
    }

    public void setCourseMetricModels(List<CourseMetricModel> courseMetricModels) {
        this.courseMetricModels = courseMetricModels;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = Float.parseFloat(bonus);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCredits() {
        return credits;
    }

    public String getCreditsAsString() {
        return String.valueOf(credits);
    }

    public void setCredits(String credits) {
        this.credits = Float.parseFloat(credits);
    }

    public float getOverall() {
        return overall;
    }

    public String getOverallAsString() {
        return String.valueOf(overall);
    }

    public void setOverall(String overall) {
        this.overall = Float.parseFloat(overall);
    }

    public float getAverage() {
        return overall * credits;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }

    public void setCredits(float credits) {
        this.credits = credits;
    }

    public void setOverall(float overall) {
        this.overall = overall;
    }

}
