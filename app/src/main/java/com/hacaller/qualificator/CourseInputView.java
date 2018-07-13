package com.hacaller.qualificator;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class CourseInputView extends ConstraintLayout implements View.OnClickListener {

    int position;
    float score;
    float weigth;
    ImageButton btnScoreUp;
    ImageButton btnScoreDown;
    TextView txtCourse;
    EditText edtScore;
    EditText edtWeight;

    IComputeScores computeScores;

    public CourseInputView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        btnScoreUp = (ImageButton) getViewById(R.id.btnScoreUp);
        btnScoreDown = (ImageButton) getViewById(R.id.btnScoreDown);
        txtCourse = (TextView) getViewById(R.id.txtCourse);
        edtScore = (EditText) getViewById(R.id.edtScore);
        edtWeight = (EditText) getViewById(R.id.edtWeight);
        btnScoreUp.setOnClickListener(this);
        btnScoreDown.setOnClickListener(this);

        edtWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == (actionId & EditorInfo.IME_MASK_ACTION)) {
                    weigth = Float.valueOf(v.getText().toString());
                    if (computeScores != null)
                        computeScores.computeMean(position, score, weigth);
                }
                return false;
            }
        });

        edtScore.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_DONE == (actionId & EditorInfo.IME_MASK_ACTION)) {
                    score = Float.valueOf(v.getText().toString());
                    if (computeScores != null)
                        computeScores.computeMean(position, score, weigth);
                }
                return false;
            }
        });
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setIndicatorName(String name) {
        if (txtCourse != null) {
            txtCourse.setText(name);
        }
    }

    public void setScore(float score) {
        this.score = score;
        edtScore.setText(String.valueOf(score));
    }

    public void setWeigth(float weigth) {
        this.weigth = weigth;
        edtWeight.setText(String.valueOf(weigth));
    }


    public void setComputeScores(IComputeScores computeScores) {
        this.computeScores = computeScores;

    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnScoreDown)) {
            score -= 0.5;
            edtScore.setText(String.valueOf(score));
            if (computeScores != null)
                computeScores.computeMean(position, score, weigth);
        }
        if (v.equals(btnScoreUp)) {
            score += 0.5;
            edtScore.setText(String.valueOf(score));
            if (computeScores != null)
                computeScores.computeMean(position, score, weigth);
        }

    }

    public float getScore() {
        score = Float.parseFloat(edtScore.getText().toString());
        return score;
    }

    public float getWeight() {
        weigth = Float.parseFloat(edtWeight.getText().toString());
        return weigth;
    }

    public void showWeight(boolean isVisible) {
        if (isVisible) {
            edtWeight.setVisibility(VISIBLE);
        } else {
            edtWeight.setVisibility(INVISIBLE);
        }
    }
}
