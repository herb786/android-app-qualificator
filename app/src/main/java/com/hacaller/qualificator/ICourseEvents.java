package com.hacaller.qualificator;

public interface ICourseEvents {
    void onClickEditCourse(int pos, CourseModel courseModel);
    void onClickRemoveCourse(int pos);
}
