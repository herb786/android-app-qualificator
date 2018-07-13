package com.hacaller.qualificator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewHolder> {

    List<CourseModel> courseModelList;
    ICourseEvents courseEvents;

    public CourseViewAdapter(List<CourseModel> courseModelList) {
        this.courseModelList = courseModelList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item_layout, parent, false);
        return new CourseViewHolder(view, courseEvents);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.bindData(position, courseModelList.get(position));
    }

    @Override

    public int getItemCount() {
        return courseModelList.size();
    }

    public void setCourseModelList(List<CourseModel> courseModelList) {
        this.courseModelList.clear();
        this.courseModelList.addAll(courseModelList);
        notifyDataSetChanged();
    }

    public void setCourseEvents(ICourseEvents courseEvents) {
        this.courseEvents = courseEvents;
    }
}
