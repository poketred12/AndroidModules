package com.example.finalprojecttest.module;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;

/**
 * 지오코더와 GPS 를 이용하여, 위도 경도를 알아내 상세 주소를 알아내도록 함.
 * 안 쓸때는 destroy() 메서드를 사용하여 메모리를 제거해줘야함.
 */

public class GPSLocationHandler {
    private Context context;
    private Geocoder geocoder;
    private LocationManager lm;
    private double currentLongitude=0.0;
    private double currentLatitude =0.0;
    private double currentAltitude =0.0;
    private float currentAccuracy=0.0f;
    private String provider="";
    private List<Address> numberToaddress =null;
    private List<Address> addressTonumber =null;
    public GPSLocationHandler(Context context){
        this.context = context;
        geocoder= new Geocoder(context);
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        try{
             lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                     1, // 통지사이의 최소 변경거리 (m)
                     mLocationListener);
             lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                     100, // 통지사이의 최소 시간간격 (miliSecond)
                     1, // 통지사이의 최소 변경거리 (m)
                     mLocationListener);
        }
        catch(SecurityException ex){
        }
    }
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            //Log.d("test", "onLocationChanged, location:" + location);
            currentLongitude = location.getLongitude(); //경도
            currentLatitude = location.getLatitude();   //위도
            currentAltitude = location.getAltitude();   //고도
            currentAccuracy = location.getAccuracy();    //정확도
            provider = location.getProvider();   //위치제공자
            try{
                numberToaddress = geocoder.getFromLocation(
                        currentLatitude, // 위도
                        currentLongitude, // 경도
                        10); // 얻어올 값의 개수
            }catch (IOException e){

            }

            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
        }
        public void onProviderDisabled(String provider) {
            // Disabled시
         //   Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
       //     Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
         //   Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };//LocationListener
    public List<Address> getNumberToAddress (){
        return numberToaddress;
    }
    public List<Address> getNumberToAddress (double latitude ,double longtitude){
        List<Address> list =null;
        try{
            list = geocoder.getFromLocation(
                    latitude, // 위도
                    longtitude, // 경도
                    10); // 얻어올 값의 개수
        }catch (IOException e){

        }
        return list;
    }
    public List<Address> getAddressToNumber (String str){
        try {
            addressTonumber = geocoder.getFromLocationName
                    (str, // 지역 이름
                            10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
           // Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        return addressTonumber;
    }
    public void destroy(){
        try{
            lm.removeUpdates(mLocationListener);
        }catch (SecurityException se){
        }
    }

}
