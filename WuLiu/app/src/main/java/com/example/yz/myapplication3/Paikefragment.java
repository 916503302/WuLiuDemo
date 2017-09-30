package com.example.yz.myapplication3;


import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.example.yz.myapplication3.map.MapConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yz on 2017/3/2.
 */
public class Paikefragment extends Fragment implements LocationSource, AMapLocationListener,
        AMap.OnMarkerClickListener {

    private MapView mMapView;
    private View mapLayout;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mapLocationClient;
    private Marker marker1;
    private Marker marker2;
    private Marker marker3;
    private double[] coords;//路线坐标点数据,不断清空复用
    private List<LatLng> carsLatLng;//静态车辆的数据
    private List<LatLng> goLatLng;
    private List<Marker> showMarks;//静态车辆图标
    private List<SmoothMoveMarker> smoothMarkers;//平滑移动图标集合
    //经度
    private double lng = 0.0;
    //纬度
    private double lat = 0.0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mapLayout == null) {
//            mapLayout = inflater.inflate(R.layout.activity_paike, container, false);
            mapLayout = inflater.inflate(R.layout.activity_paike, null);
            mMapView = (MapView) mapLayout.findViewById(R.id.map);
            //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
            mMapView.onCreate(savedInstanceState);
            if (aMap == null) {
                aMap = mMapView.getMap();
                //设置初始化中心点
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(MapConstants.BEIJING));
                //设置初始化地图缩放级别，数字越小显示地图范围越大
                aMap.moveCamera(CameraUpdateFactory.zoomTo(11));
                setUpMap();
            } else {
                if (mapLayout.getParent() != null) {
                    ((ViewGroup) mapLayout.getParent()).removeView(mapLayout);
                }
            }
        }

                initView();
             initData();

        return mapLayout;
    }

    private void initView() {
        Button put = (Button)mapLayout .findViewById(R.id.put);
        Button run = (Button)mapLayout. findViewById(R.id.run);

        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"123",Toast.LENGTH_SHORT).show();
            }
        });

        //放入静态车辆
        put.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getActivity(),"123",Toast.LENGTH_SHORT).show();
                //清空地图覆盖物
             aMap.clear();
                //清除旧集合
                if (showMarks == null) {
                    showMarks = new ArrayList<Marker>();
                }
                for (int j = 0; j < showMarks.size(); j++) {
                    showMarks.get(j).remove();
                }
                //依次放入静态图标
                for (int i = 0; i < carsLatLng.size(); i++) {
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.map_icon_driver_car);
                    lng = Double.valueOf(carsLatLng.get(i).longitude);
                    lat = Double.valueOf(carsLatLng.get(i).latitude);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(lat, lng))
                            .icon(icon);
                    showMarks.add(aMap.addMarker(markerOptions));

                    Animation startAnimation = new AlphaAnimation(0, 1);
                    startAnimation.setDuration(600);
                    //设置所有静止车的角度
//                            showMarks.get(i).setRotateAngle(Float.valueOf(listBaseBean.datas.get(i).angle));
                    showMarks.get(i).setAnimation(startAnimation);
                    showMarks.get(i).setRotateAngle(new Random().nextInt(359));
                    showMarks.get(i).startAnimation();
                }
            }
        });

        /**
         * 展示平滑移动车辆
         */
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aMap.clear();//清空地图覆盖物
                smoothMarkers = null;//清空旧数据
                smoothMarkers = new ArrayList<SmoothMoveMarker>();

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.map_icon_driver_car);
                //循环
                for (int i = 0; i < carsLatLng.size(); i++) {
                    //放入路线
                    double[] newoords = {Double.valueOf(carsLatLng.get(i).longitude), Double.valueOf(carsLatLng.get(i).latitude),
                            Double.valueOf(goLatLng.get(i).longitude), Double.valueOf(goLatLng.get(i).latitude)};
                    coords = newoords;
                    //移动车辆
                    movePoint(icon);
                }
            }
        });
    }
    //平滑移动
    public void movePoint(BitmapDescriptor bitmap) {
        // 获取轨迹坐标点
        List<LatLng> points = readLatLngs();
//        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
//        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

//        SparseArrayCompat sparseArrayCompat = new SparseArrayCompat();//谷歌新集合，替代hashmap
        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
        smoothMarkers.add(smoothMarker);
        int num = smoothMarkers.size() - 1;
        // 设置滑动的图标
        smoothMarkers.get(num).setDescriptor(bitmap);
        LatLng drivePoint = points.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

        // 设置滑动的轨迹左边点
        smoothMarkers.get(num).setPoints(subList);
        // 设置滑动的总时间
        smoothMarkers.get(num).setTotalDuration(19);
        // 开始滑动
        smoothMarkers.get(num).startSmoothMove();
    }
    //获取路线
    private List<LatLng> readLatLngs() {
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < coords.length; i += 2) {
            points.add(new LatLng(coords[i + 1], coords[i]));
        }
        return points;
    }

    private void initData() {
        //出发地
        LatLng car1 = new LatLng(39.902138,116.391415 );
        LatLng car2 = new LatLng(39.935184,116.328587 );
        LatLng car3 = new LatLng(39.987814,116.488232 );
        LatLng car4 = new LatLng(39.919814,116.488432 );
        LatLng car5 = new LatLng(39.914814,116.188432 );
        LatLng car6 = new LatLng(39.86758762,116.34315491 );
        //出发地坐标集合
        LatLng car7 = new LatLng(39.79376521,116.39465332 );
        LatLng car8= new LatLng(40.05494994,116.275177 );
        carsLatLng = new ArrayList<>();
        carsLatLng.add(car1);
        carsLatLng.add(car2);
        carsLatLng.add(car3);
        carsLatLng.add(car4);
        carsLatLng.add(car5);
        carsLatLng.add(car6);
        carsLatLng.add(car7);
        carsLatLng.add(car8);
        //目的地
        LatLng go1 = new LatLng(39.96782,116.403775);
        LatLng go2 = new LatLng(39.891225,116.325235);
        LatLng go3 = new LatLng(39.883322,116.415619 );
        LatLng go4= new LatLng(39.883022,116.325619 );
        LatLng go5= new LatLng(39.783092,116.395619 );
        LatLng go6= new LatLng(40.04654019,116.39533997 );
        LatLng go7= new LatLng(39.91974272,116.29646301 );
        LatLng go8= new LatLng(39.80220607,116.47361755 );
        //目的地坐标集合
        goLatLng = new ArrayList<>();
        goLatLng.add(go1);
        goLatLng.add(go2);
        goLatLng.add(go3);
        goLatLng.add(go4);
        goLatLng.add(go5);
        goLatLng.add(go6);
        goLatLng.add(go7);
        goLatLng.add(go8);
    }
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(marker1)) {
            String markerTitle = marker1.getTitle();
            //WaterDialog(markerTitle);
             Toast.makeText(getActivity(), "点击获取当前信息", Toast.LENGTH_SHORT).show();
        }
        else if(marker.equals(marker2)){
            String markerTitle = marker2.getTitle();
           // WaterDialog(markerTitle);
        }else if(marker.equals(marker3)){
            String markerTitle=marker3.getTitle();
          //  WaterDialog(markerTitle);
        }
        return true;
    }


    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_arrows));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setLocationSource(locationSource);
        aMap.setOnMarkerClickListener(this);
        addMarkersToMap();
    }

    /**
     * 在地图上添加markerlocation_arrows.png
     */
    private void addMarkersToMap() {
        MarkerOptions markop1 = new MarkerOptions();
        markop1.anchor(0.5f, 0.5f)
                .position(MapConstants.BEIJING).title("北京市政府")
                .draggable(true);
        markop1.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.common_icon_tips_green)));
        marker1 = aMap.addMarker(markop1);


        MarkerOptions markop2 = new MarkerOptions();
        markop2.anchor(0.5f, 0.5f)
                .position(MapConstants.ZHONGGUANCUN).title("北京中关村")
                .draggable(true);
        markop2.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.common_icon_tips_green)));
        marker2 = aMap.addMarker(markop2);

        MarkerOptions markop3 = new MarkerOptions();
        markop3.anchor(0.5f, 0.5f)
                .position(MapConstants.QINGHUA).title("清华")
                .draggable(true);
        markop3.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.common_icon_tips_green)));
        marker3 = aMap.addMarker(markop3);
    }


    private final LocationSource locationSource = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
            if (mapLocationClient == null) {
                mapLocationClient = new AMapLocationClient(Paikefragment.this.getContext());
                AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                //设置定位监听
                mapLocationClient.setLocationListener(Paikefragment.this);
                mLocationOption.setNeedAddress(true);
                //设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置定位参数
                mapLocationClient.setLocationOption(mLocationOption);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                mapLocationClient.startLocation();
            }
        }

        @Override
        public void deactivate() {
            mListener = null;
            if (mapLocationClient != null) {
                mapLocationClient.stopLocation();
                mapLocationClient.onDestroy();
            }
            mapLocationClient = null;
        }

    };

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }


}
