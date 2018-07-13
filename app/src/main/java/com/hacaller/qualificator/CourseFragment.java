package com.hacaller.qualificator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class CourseFragment extends Fragment implements IComputeScores {

    @BindViews({R.id.input1, R.id.input2, R.id.input3, R.id.input4, R.id.input5, R.id.input6, R.id.input7})
    List<CourseInputView> courseInputViews;

    @BindView(R.id.edtCourseName)
    EditText edtCourseName;

    @BindView(R.id.edtCourseCredit)
    EditText edtCourseCredit;

    @BindView(R.id.txtCursoPromedio)
    TextView txtCursoPromedio;

    @BindView(R.id.btnMeanDown)
    ImageButton btnMeanDown;

    @BindView(R.id.btnMeanUp)
    ImageButton btnMeanUp;

    @BindView(R.id.btnSaveCourse)
    Button btnSaveCourse;

    int position;
    CourseModel courseModel;
    List<CourseMetricModel> metrics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View myCourseView = inflater.inflate(R.layout.fragment_curso_layout, container, false);
        ButterKnife.bind(this, myCourseView);
        courseModel = ((QualificatorActivity) getActivity()).getCurrentCourseModel();
        edtCourseName.setText(courseModel.getName());
        edtCourseCredit.setText(courseModel.getCreditsAsString());
        txtCursoPromedio.setText(courseModel.getOverallAsString());
        int pos = 0;
        metrics = courseModel.getCourseMetricModels();
        List<String> indictaors = Arrays.asList(getResources().getStringArray(R.array.indicadores));
        for (CourseMetricModel metric : metrics) {
            courseInputViews.get(pos).setIndicatorName(indictaors.get(pos));
            courseInputViews.get(pos).setScore(metric.getScore());
            courseInputViews.get(pos).setWeigth(metric.getWeight());
            courseInputViews.get(pos).setComputeScores(this);
            courseInputViews.get(pos).setPosition(pos);
            pos++;
        }
        courseInputViews.get(6).setIndicatorName(indictaors.get(6));
        courseInputViews.get(6).showWeight(false);
        courseInputViews.get(6).setScore(courseModel.getBonus());
        courseInputViews.get(6).setComputeScores(this);
        courseInputViews.get(6).setPosition(6);

        calculateMean();


        //<editor-fold des=changeName>
        edtCourseName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == (actionId & EditorInfo.IME_MASK_ACTION)) {
                    courseModel.setName(v.getText().toString());
                }
                return false;
            }
        });
        //</editor-fold>

        //<editor-fold des=changeCredit>
        edtCourseCredit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == (actionId & EditorInfo.IME_MASK_ACTION)) {
                    courseModel.setCredits(v.getText().toString());
                }
                return false;
            }
        });
        //</editor-fold>

        //<editor-fold desc=btnPromedio>

        btnMeanDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                float mean = Float.valueOf(txtCursoPromedio.getText().toString());
                mean -= 0.5;
                txtCursoPromedio.setText(String.valueOf(mean));
                courseModel.setOverall(String.valueOf(mean));
            }
        });

        btnMeanUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                float mean = Float.valueOf(txtCursoPromedio.getText().toString());
                mean += 0.5;
                txtCursoPromedio.setText(String.valueOf(mean));
                courseModel.setOverall(String.valueOf(mean));
            }
        });

        //</editor-fold>

        //<editor-fold desc=btnGuardar>

        btnSaveCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnSaveCourse.setBackgroundResource(R.drawable.btnguardar);
                btnSaveCourse.setPadding(0, 0, 0, 0);
                ((QualificatorActivity) getActivity()).updateCourseModel(position, courseModel);
            }
        });

        //</editor-fold>

        return myCourseView;
    }

    @Override
    public void computeMean(int pos, float score, float weight) {
        metrics.get(pos).setScore(score);
        metrics.get(pos).setWeight(weight);
        calculateMean();
    }

    public void calculateMean() {
        int i = 0;
        float weigth = 0;
        float score = 0;
        float average = 0;
        while (i < 6) {
            weigth = metrics.get(i).getWeight();
            score = metrics.get(i).getScore();
            average += weigth * score / 100;
            ++i;
        }
        average += courseModel.getBonus();
        txtCursoPromedio.setText(String.format(Locale.ENGLISH, "%.1f", average));
        courseModel.setOverall(average);
    }
}
