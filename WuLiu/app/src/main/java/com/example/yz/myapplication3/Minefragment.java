package com.example.yz.myapplication3;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

;

/**
 * Created by yz on 2017/3/3.
 */
public class Minefragment extends Fragment implements View.OnClickListener {
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mine, container, false);
        init();
        return view;

    }
    private void init(){
        Button teoperature =(Button)view.findViewById(R.id.temperature);
        teoperature.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent =new Intent();
        intent.setClass(getActivity(),
                temperatureData.class);
        startActivity(intent);

    }
}
