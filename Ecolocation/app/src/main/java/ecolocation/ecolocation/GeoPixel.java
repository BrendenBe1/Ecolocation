package ecolocation.ecolocation;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Chandler on 12/8/2017.
 */

public class GeoPixel {
    //coordinates to make a pixel
    private LatLng center;  //the location of the center pixel
    private LatLng topLeft;
    private LatLng topRight;
    private LatLng bottomLeft;
    private LatLng bottomRight;

    private double offset;  //the offset is how much
    private int value;   //the value --> nutrient distribution for the center pixel

    public GeoPixel(LatLng center, double offset, int value) {
        this.offset = offset;
        this.value = value;

        this.center = center;
        double latitude = center.latitude;
        double longitude = center.longitude;

        //calculate corner coordinates of the pixel
        double topCoordinate = calcTopCoordinate();
        double bottomCoordinate = calcBottomCoordinate();
        double leftCoordinate = calcLeftCoordinate();
        double rightCoordinate = calcRightCoordinate();

        topLeft = new LatLng(topCoordinate, leftCoordinate);
        topRight = new LatLng(topCoordinate, rightCoordinate);
        bottomLeft = new LatLng(bottomCoordinate, leftCoordinate);
        bottomRight = new LatLng(bottomCoordinate, rightCoordinate);
    }

    //find latitude of top coordinates
    private double calcTopCoordinate(){
        double centerLat = center.latitude;
        double latitude;

        //if centerLat is positive
        //make sure it won't go past 90 degree limit for latitude
        if(centerLat + offset > 90){
            //centerLat goes into the negative side of the equator
            latitude = 90 - (centerLat+offset);
        }
        //normal case
        else{
            latitude = centerLat + offset;
        }

        return latitude;
    }

    //find latitude of bottom coordinates
    private double calcBottomCoordinate(){
        double centerLat = center.latitude;
        double latitude;

        //make sure it won't go past the -90 degree limit for latitude
        if(centerLat - offset < -90){
            latitude = 90 + (centerLat-offset);
        }
        //normal case
        else{
            latitude = centerLat - offset;
        }
        return latitude;
    }

    //find longitude of left coordinates
    private double calcLeftCoordinate(){
        double centerLong = center.longitude;
        double longitude;

        //make sure it won't go past the -180 degree limit for longitude
        if(centerLong - offset < -180){
            longitude = 180 + (centerLong - offset);
        }
        //normal case
        else{
            longitude = centerLong - offset;
        }

        return longitude;
    }

    //find longitude of right coordinates
    private double calcRightCoordinate(){
        double centerLong = center.longitude;
        double longitude;

        //make sure it won't go past the 180 degree limit for longitude
        if(centerLong + offset > 180){
            longitude = 180 - (centerLong + offset);
        }
        //normal case
        else {
            longitude = centerLong + offset;
        }

        return longitude;
    }

    public LatLng getCenter() {
        return center;
    }

    public LatLng getTopLeft() {
        return topLeft;
    }

    public LatLng getTopRight() {
        return topRight;
    }

    public LatLng getBottomLeft() {
        return bottomLeft;
    }

    public LatLng getBottomRight() {
        return bottomRight;
    }

    public int getValue(){
        return value;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(int newVal){
        offset = newVal;
    }


}
