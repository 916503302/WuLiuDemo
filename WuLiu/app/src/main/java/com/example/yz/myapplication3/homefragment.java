package com.example.yz.myapplication3;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yz.myapplication3.subfragment.SubFragment1;
import com.example.yz.myapplication3.subfragment.SubFragment2;
import com.example.yz.myapplication3.subfragment.SubFragment3;

import static com.example.yz.myapplication3.R.id.companyInfo;

/**
 * Created by yz on 2017/3/2.
 */
public class homefragment extends Fragment implements View.OnClickListener{

    FragmentTabHost mTabHost = null;
    private LinearLayout companyInfo;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment, container, false);
        initView(view);
        LinearLayout companyInfo =(LinearLayout)view.findViewById(R.id.companyInfo);
        companyInfo.setOnClickListener(this);
        return view;
    }
    private void initView(View view) {

        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);

        mTabHost.setup(getActivity(), getChildFragmentManager(),
                android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("sub1").setIndicator("一号仓库"),
                SubFragment1.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("sub2").setIndicator("二号仓库"),
                SubFragment2.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("sub3").setIndicator("三号仓库"),
                SubFragment3.class, null);


    }



    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        intent.setClass(getActivity(), companyInformation.class);
        startActivity(intent);
    }

}


