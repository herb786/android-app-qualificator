package com.hacaller.qualificator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("COURSEPOSITION");

    }

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
                    if (v.getText().toString().length() > 0 && v.getText().charAt(0) != 0x2e) {
                        courseModel.setName(v.getText().toString());
                        ((QualificatorActivity) getActivity()).updateCourseModel(position, courseModel);
                    }

                }
                return false;
            }
        });
        //</editor-fold>

        //<editor-fold des=changeCredit>
        edtCourseCredit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0  && s.toString().charAt(0) != 0x2e){
                    courseModel.setCredits(s.toString());
                    ((QualificatorActivity) getActivity()).updateCourseModel(position, courseModel);
                }

            }
        });

        edtCourseCredit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == (actionId & EditorInfo.IME_MASK_ACTION)) {
                    if (v.getText().toString().length() > 0 && v.getText().charAt(0) != 0x2e) {
                        courseModel.setCredits(v.getText().toString());
                        ((QualificatorActivity) getActivity()).updateCourseModel(position, courseModel);
                    }

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
                ((QualificatorActivity) getActivity()).updateCourseModel(position, courseModel);

            }
        });

        btnMeanUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                float mean = Float.valueOf(txtCursoPromedio.getText().toString());
                mean += 0.5;
                txtCursoPromedio.setText(String.valueOf(mean));
                courseModel.setOverall(String.valueOf(mean));
                ((QualificatorActivity) getActivity()).updateCourseModel(position, courseModel);

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
        courseModel.calculatOverall();
        calculateMean();
    }

    public void calculateMean() {
        txtCursoPromedio.setText(String.format(Locale.ENGLISH, "%.1f", courseModel.getOverall()));
        ((QualificatorActivity) getActivity()).updateCourseModel(position, courseModel);

    }
}
