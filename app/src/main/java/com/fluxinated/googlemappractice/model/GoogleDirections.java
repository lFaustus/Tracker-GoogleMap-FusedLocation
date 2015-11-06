package com.fluxinated.googlemappractice.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Fluxi on 11/6/2015.
 */
public class GoogleDirections
{
    private String distance;
    private String duration;
    private String endAddress;
    private LatLng endLocation;
    private String startAddress;
    private LatLng startLocation;
    private String overview_polyline;
    private String travel_mode;
    private Long departure_time;
    private Long arrival_time;

    public GoogleDirections(){}

    public void setDistance(String distance)
    {
        this.distance = distance;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    public void setEndAddress(String endAddress)
    {
        this.endAddress = endAddress;
    }

    public void setEndLocation(double lat,double lng)
    {
        this.endLocation = new LatLng(lat,lng);
    }

    public void setStartAddress(String startAddress)
    {
        this.startAddress = startAddress;
    }

    public void setStartLocation(double lat,double lng)
    {
        this.startLocation = new LatLng(lat,lng);
    }

    public void setOverviewPolyline(String overview_polyline)
    {
        this.overview_polyline = overview_polyline;
    }

    public void setTravelMode(String travel_mode)
    {
        this.travel_mode = travel_mode;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public String getOverview_polyline() {
        return overview_polyline;
    }

    public String getTravel_mode() {
        return travel_mode;
    }

    public Long getDepartureTime() {
        return departure_time;
    }

    public void setDeparture_time(Long DepartureTime) {
        this.departure_time = departure_time;
    }

    public Long getArrivalTime() {
        return arrival_time;
    }

    public void setArrivalTime(Long arrivaltime) {
        this.arrival_time = arrivaltime;
    }

    @Override
    public String toString() {
        return "Distance: " + distance +" Duration: " + duration +" EndAddress: "+ endAddress + " EndLocation: " + endLocation.toString()
                + " startAddress: " + startAddress + " startLocation: " + startLocation.toString() + " polyline points: " +overview_polyline
                + " Travel Mode: " + travel_mode;
    }
}
