package net.kevin.com.healthmanager.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import net.kevin.com.healthmanager.R;
import net.kevin.com.healthmanager.activity.DynamicDemo;

import net.kevin.com.healthmanager.activity.WeightActivity;
import net.kevin.com.healthmanager.javaBean.User;
import net.kevin.com.healthmanager.step.StepArcView;


import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;


/**
 *首页
 */
public class FirstFragment extends Fragment {


    private StepArcView stepArcView;
    private SharedPreferences sharedPreferences;
    private Button btn_start,btn_weight;
    private TextView tv_plan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        stepArcView = (StepArcView) view.findViewById(R.id.step);
        btn_start = (Button) view.findViewById(R.id.start_running);
        btn_weight = (Button) view.findViewById(R.id.weight);
        tv_plan = (TextView) view.findViewById(R.id.tv_plan);

        stepArcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);*/
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(), DynamicDemo.class);
                    startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

            }
        });

        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WeightActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }




    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        User user = BmobUser.getCurrentUser(User.class);
        int userStep = 0;
        if (user.getStep()!=null) {
            userStep = Integer.parseInt(user.getStep().get(user.getStep().size()-1));
        }
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = sdf.format(date);
        sharedPreferences = getActivity().getSharedPreferences("runStep", Context.MODE_PRIVATE);
        String time = sharedPreferences.getString("time","2018");
        int stepPlan = sharedPreferences.getInt("plan",10000);
        tv_plan.setText(stepPlan+"");
        if (time.equals(currentTime)){
            int step = sharedPreferences.getInt("step",0);
            if (user.getStepDate()!=null && currentTime.equals(user.getStepDate().get(user.getStepDate().size()-1))) {
                if (step>userStep) {
                    stepArcView.setCurrentCount(stepPlan, step);
                } else {
                    stepArcView.setCurrentCount(stepPlan, userStep);
                }
            } else {
                stepArcView.setCurrentCount(stepPlan, 0);
            }


        } else {
            SharedPreferences myPreference = getActivity().getSharedPreferences("runStep", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPreference.edit();
            editor.putInt("plan", 10000);
            editor.putString("time",currentTime);
            editor.putInt("step",0);
            editor.commit();
            stepArcView.setCurrentCount(10000, 0);
        }

    }

}
