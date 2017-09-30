package com.example.yz.myapplication3;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private homefragment homefragment;
    private Paikefragment paikefragment;
    private Myloginfragment myloginfragment;
    private Minefragment minefragment;
    private TextView sy;
    private TextView sx;
    private TextView pk;
    private TextView mine;
    private ImageView pic_shuiqing;
    private ImageView pic_shuixi;
    private ImageView pic_shuile;
    private ImageView pic_mine;
    private android.support.v4.app.FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {//避免fragment重叠
            fragmentManager = getSupportFragmentManager();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            initView(); // 初始化界面控件
            Log.e("aaa", "123");
            setChioceItem(0);
        }
    }

    private void initView() {
        // 初始化页面标题栏
//        ImageView titleLeftImv = (ImageView) findViewById(R.id.title_imv);
//        titleLeftImv.setVisibility(View.INVISIBLE);
        //titleTv = (TextView) findViewById(R.id.title_text_tv);

        // 初始化底部导航栏的控件
        sy = (TextView) findViewById(R.id.tab_shouye_tv);
        sx = (TextView) findViewById(R.id.tab_shuixi_tv);
        pk = (TextView) findViewById(R.id.tab_paike_tv);
       mine = (TextView) findViewById(R.id.tab_mine_tv);

        LinearLayout shouye = (LinearLayout) findViewById(R.id.tab_shouye);
        LinearLayout shuixi = (LinearLayout) findViewById(R.id.tab_shuixi);
       LinearLayout paike = (LinearLayout) findViewById(R.id.tab_paike);
     LinearLayout wode = (LinearLayout) findViewById(R.id.tab_mylogin);

        pic_shuiqing = (ImageView) findViewById(R.id.pic_shuiqing);
        pic_shuixi = (ImageView) findViewById(R.id.pic_shuixi);
       pic_shuile = (ImageView) findViewById(R.id.pic_shuile);
       pic_mine = (ImageView) findViewById(R.id.pic_mine);

        shouye.setOnClickListener(MainActivity.this);
        shuixi.setOnClickListener(MainActivity.this);
        paike.setOnClickListener(MainActivity.this);
       wode.setOnClickListener(MainActivity.this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tab_shouye:
                // GoToThree = false;
                setChioceItem(0);
                break;
            case R.id.tab_shuixi:
                //  GoToThree = false;
                setChioceItem(1);
                break;
            case R.id.tab_paike:
               // GoToThree = false;
                setChioceItem(2);
               break;
           case R.id.tab_mylogin:
               //  GoToThree = true;
               setChioceItem(3);
                break;

        }

    }

    private void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        clearChioce(); // 清空, 重置选项, 隐藏所有Fragment

        switch (index) {
            case 0:
                //titleTv.setText("水 情");
                sy.setTextColor(getResources().getColor(R.color.waterblue));
                pic_shuiqing.setImageDrawable(getResources().getDrawable(R.drawable.shishi_pre));
                if (homefragment == null) {
                    homefragment = new homefragment();
                    fragmentTransaction.add(R.id.content, homefragment);  //content下创建mapFragment
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(homefragment);
                }
                Toast.makeText(this,"第一个界面",Toast.LENGTH_SHORT).show();
                break;
            case 1:
               // titleTv.setText("水 系");
                sx.setTextColor(getResources().getColor(R.color.waterblue));
                pic_shuixi.setImageDrawable(getResources().getDrawable(R.drawable.yujing_pre));
                if (paikefragment == null) {
                    paikefragment = new Paikefragment();
                    fragmentTransaction.add(R.id.content, paikefragment);  //content下创建mapFragment

                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(paikefragment);
                }
                //获取数据
//                RiverSystemTool riverSystemTool = new RiverSystemTool(mapFragment);
//                riverSystemTool.GetWaterInfoInfo();
                Toast.makeText(this,"第二个界面",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                //titleTv.setText("水 乐");
                pk.setTextColor(getResources().getColor(R.color.waterblue));
                pic_shuile.setImageDrawable(getResources().getDrawable(R.drawable.paike_pre));
               if (minefragment == null) {
                   minefragment = new Minefragment();
                   fragmentTransaction.add(R.id.content, minefragment);
               } else {
                   // 如果不为空，则直接将它显示出来
                   fragmentTransaction.show(minefragment);
                }
                Toast.makeText(this,"第三个界面",Toast.LENGTH_SHORT).show();
               break;
           case 3:
//                if (!IsLogin()) {
//                    //titleTv.setText("个人中心");
                    mine.setTextColor(getResources().getColor(R.color.waterblue));
                    pic_mine.setImageDrawable(getResources().getDrawable(R.drawable.mine_pre));

                   if (myloginfragment == null) {
                       myloginfragment = new Myloginfragment();
                      fragmentTransaction.add(R.id.content, myloginfragment);
                   } else {
                       // 如果不为空，则直接将它显示出来
                       fragmentTransaction.show(myloginfragment);
                    }
               //获取数据
//                RiverSystemTool riverSystemTool = new RiverSystemTool(mapFragment);
//                riverSystemTool.GetWaterInfoInfo();
               Toast.makeText(this,"第四个界面",Toast.LENGTH_SHORT).show();
               break;
//                } else {
//                    //titleTv.setText("个人中心");
//                    mine.setTextColor(getResources().getColor(R.color.waterblue));
//                    pic_mine.setImageDrawable(getResources().getDrawable(R.drawable.mine_pre));
//
//                    if (mineFragment == null) {
//                        mineFragment = new MineFragment();
//                        fragmentTransaction.add(R.id.content, mineFragment);
//                    } else {
//                        // 如果不为空，则直接将它显示出来
//                        fragmentTransaction.show(mineFragment);
//                    }
//                }
             //   break;
        }
        fragmentTransaction.commit(); // 提交
    }

    /**
     * 隐藏Fragment
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (homefragment != null) {
            fragmentTransaction.hide(homefragment);
        }
        if (paikefragment != null) {
            fragmentTransaction.hide(paikefragment);
        }
//        if (homeFragment != null) {
//            fragmentTransaction.hide(homeFragment);
//        }
        if (minefragment != null) {
            fragmentTransaction.hide(minefragment);
       }
       if (myloginfragment != null) {
            fragmentTransaction.hide(myloginfragment);
       }

    }

    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认
     */
    private void clearChioce() {

        sy.setTextColor(getResources().getColor(R.color.unchoose));
        sx.setTextColor(getResources().getColor(R.color.unchoose));
        pk.setTextColor(getResources().getColor(R.color.unchoose));
        mine.setTextColor(getResources().getColor(R.color.unchoose));
        pic_shuiqing.setImageDrawable(getResources().getDrawable(R.drawable.shishi));
        pic_shuixi.setImageDrawable(getResources().getDrawable(R.drawable.yujing));
        pic_shuile.setImageDrawable(getResources().getDrawable(R.drawable.paike));
        pic_mine.setImageDrawable(getResources().getDrawable(R.drawable.mine));

    }


}
