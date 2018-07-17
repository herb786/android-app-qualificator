package com.hacaller.qualificator;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QualificatorActivity extends FragmentActivity implements ICourseEvents {

    public final String TAG = getClass().getCanonicalName();

    public static boolean handset = false;

    @BindView(R.id.courseList)
    RecyclerView recyclerView;

    @BindView(R.id.txtTotalAvg)
    TextView txtTotalAvg;

    List<CourseModel> courseModels;
    CourseViewAdapter adapter;
    CourseModel courseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualificator);
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        ButterKnife.bind(this);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        {
            openCourses();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 999);
            }
        }

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.setAdSize(AdSize.BANNER);
        //mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        mAdView.loadAd(adRequest);
    }

    public void loadCourses(){
        if (courseModels != null && courseModels.size() > 0) {
            courseModel = courseModels.get(0);
            adapter = new CourseViewAdapter(courseModels, this);
            recyclerView.setAdapter(adapter);
            txtTotalAvg.setText(String.format(Locale.ENGLISH, "%.2f", totalAvg()));
        }
    }


    public CourseModel getCurrentCourseModel() {
        return courseModel;
    }

    public void setCurrentCourseModel(int position) {
        courseModel = courseModels.get(position);
    }

    public void updateCourseModel(int position, CourseModel courseModel) {
        courseModels.set(position, courseModel);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClickEditCourse(int pos, CourseModel courseModel) {
        setCurrentCourseModel(pos);
        CourseFragment courseFragment = new CourseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("COURSEPOSITION", pos);
        courseFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, courseFragment)
                .addToBackStack("CourseFragment")
                .commit();
    }

    @Override
    public void onClickRemoveCourse(int pos) {
        clearCourse(pos);
        adapter.notifyDataSetChanged();
    }

    public float totalAvg() {
        int i = 0;
        float tcredit = 0;
        float average = 0, counter = 10;
        while (i < 10) {
            if (courseModels.get(i).equals("Nuevo")) {
                counter -= 1;
            }
            average += courseModels.get(i).getAverage();
            tcredit += courseModels.get(i).getCredits();
            ++i;
        }
        return average / tcredit;
    }

    public void openCourses() {
        try {
            InputStream file = this.getResources().openRawResource(R.raw.cursos);
            File filename = new File(getFilesDir(), "courses.csv");
            if (filename.exists()) {
                file = new FileInputStream(filename);
                Log.d(TAG, "Loading file...");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String line;
            int j = 0;
            courseModels = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                CourseModel courseModel = new CourseModel();
                StringTokenizer entry = new StringTokenizer(line, ",");
                courseModel.setName(entry.nextToken());
                Log.d(TAG, courseModel.getName());
                courseModel.setCredits(entry.nextToken());
                Log.d(TAG, String.valueOf(courseModel.getCredits()));
                List<CourseMetricModel> metricModels = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    CourseMetricModel courseMetricModel =
                            new CourseMetricModel(entry.nextToken(), entry.nextToken());
                    metricModels.add(courseMetricModel);
                }
                courseModel.setCourseMetricModels(metricModels);
                courseModel.setBonus(entry.nextToken());
                courseModel.setOverall(entry.nextToken());
                Log.d(TAG, String.valueOf(courseModel.getOverall()));
                courseModels.add(courseModel);
                ++j;
            }
            loadCourses();
            saveCourses();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public void saveCourses() {
        File filename = new File(getFilesDir(), "courses.csv");
        StringBuilder content = new StringBuilder();
        Log.d(TAG, filename.getAbsolutePath());
        try {
            OutputStream file = new FileOutputStream(filename);
            for (int j = 0; j < 10; j++) {
                CourseModel model = courseModels.get(j);
                content.append(model.getName() + ",");
                content.append(String.valueOf(model.getCredits()) + ",");
                for (int i = 0; i < 6; i++) {
                    CourseMetricModel metric = courseModels.get(j).getCourseMetricModels().get(i);
                    content.append(String.valueOf(metric.getWeight()) + ",");
                    content.append(String.valueOf(metric.getScore()) + ",");
                }
                content.append(String.valueOf(model.getBonus()) + ",");
                content.append(String.valueOf(model.getOverall()));
                byte[] bytes = content.toString().getBytes();
                file.write(bytes);
                file.write(0x0D0A);
                content.setLength(0);
            }
            file.close();
        } catch (IOException e) {
        }
    }

    public void clearCourse(int i) {
        courseModels.get(i).setName("Nuevo");
        courseModels.get(i).setCredits("0");
        courseModels.get(i).setBonus("0");
        courseModels.get(i).setOverall("0");
        List<CourseMetricModel> metrics = new ArrayList<>();
        for (int j = 0; j < 6; j++) {
            CourseMetricModel metricModel = new CourseMetricModel("0", "0");
            metrics.add(metricModel);
        }
        courseModels.get(i).setCourseMetricModels(metrics);
        saveCourses();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 999 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCourses();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qualificator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                showMessage("Tus cambios fueron guardados.");
                return true;

            case R.id.action_info:
                showMessage(String.format("Tu promedio para este semestre es %.2f", totalAvg()));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void showMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.appname));
        builder.setMessage(message);
        builder.setPositiveButton("Aceptar", null);
        builder.create().show();
    }
}



