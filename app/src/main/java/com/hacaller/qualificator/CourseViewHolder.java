package com.hacaller.qualificator;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    int position;
    CourseModel courseModel;
    ImageButton imgBtnClear;
    ImageButton imgBtnAdd;
    TextView txtName;
    TextView txtCredits;
    TextView txtGrade;
    ICourseEvents courseEvents;

    public CourseViewHolder(View itemView, ICourseEvents courseEvents) {
        super(itemView);
        this.courseEvents = courseEvents;
        imgBtnAdd = itemView.findViewById(R.id.imgBtnAdd);
        imgBtnClear = itemView.findViewById(R.id.imgBtnClear);
        txtName = itemView.findViewById(R.id.txtName);
        txtCredits = itemView.findViewById(R.id.txtCredits);
        txtGrade = itemView.findViewById(R.id.txtGrade);
        imgBtnAdd.setOnClickListener(this);
        imgBtnClear.setOnClickListener(this);
    }

    public void bindData(int pos, CourseModel courseModel){
        this.position = pos;
        this.courseModel = courseModel;
        txtName.setText(courseModel.getName());
        txtCredits.setText(courseModel.getCreditsAsString());
        txtGrade.setText(String.format("%.2f",courseModel.getOverall()));
    }

    @Override
    public void onClick(View v) {
        if (v.equals(imgBtnAdd)){
            courseEvents.onClickEditCourse(position, courseModel);
        }
        if (v.equals(imgBtnClear)){
            courseEvents.onClickRemoveCourse(position);
        }
    }
}
