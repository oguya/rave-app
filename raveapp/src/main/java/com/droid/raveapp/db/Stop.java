package com.droid.raveapp.db;

/**
 * Created by james on 25/01/14.
 */
public class Stop {
    public static final String KEY_STOP_ID = "stop_id";
    public static final String KEY_STOP_CODE = "stop_code";
    public static final String KEY_STOP_NAME = "stop_name";
    public static final String KEY_STOP_DESC = "stop_desc";
    public static final String KEY_STOP_LAT = "stop_lat";
    public static final String KEY_STOP_LON = "stop_lon";
    public static final String KEY_ZONE_ID = "zone_id";
    public static final String KEY_STOP_URL = "stop_url";
    public static final String KEY_LOCATION_TYPE = "location_type";
    public static final String KEY_PARENT_STATION ="parent_station";

    private String stop_id;
    private String stop_code;
    private String stop_name;
    private String stop_desc;
    private String stop_lat;
    private String stop_lon;
    private String zone_id;
    private String stop_url;
    private String location_type;
    private String parent_station;


    public Stop(){}

    public Stop(String stop_name, String stop_lat, String stop_lon){
        this.stop_name = stop_name;
        this.stop_lat = stop_lat;
        this.stop_lon = stop_lon;
    }

    public Stop(String _stop_id, String _stop_code, String _stop_name,
                String _stop_desc, String _stop_lat, String _stop_lon,
                String _zone_id, String _stop_url, String _location_type,
                String _parent_station){

        this.stop_id = _stop_id;
        this.stop_code = _stop_code;
        this.stop_name = _stop_name;
        this.stop_desc = _stop_desc;
        this.stop_lat = _stop_lat;
        this.stop_lon = _stop_lon;
        this.zone_id = _zone_id;
        this.stop_url = _stop_url;
        this.location_type = _location_type;
        this.parent_station = _parent_station;

    }

    public void set_stop_id(String stop_id){
        this.stop_id = stop_id;
    }

    public String get_stop_id(){
        return this.stop_id;
    }

    public void set_stop_code(String stop_code){
        this.stop_code = stop_code;
    }

    public String get_stop_code(){
        return this.stop_code;
    }

    public void set_stop_name(String stop_name){
        this.stop_name = stop_name;
    }
    public String get_stop_name(){
        return this.stop_name;
    }

    public void set_stop_desc(String stop_desc){
        this.stop_desc = stop_desc;
    }
    public String get_stop_desc(){
        return this.stop_desc;
    }

    public String get_stop_lat(){
        return this.stop_lat;
    }
    public void set_stop_lat(String stop_lat){
        this.stop_lat = stop_lat;
    }

    public String get_stop_lon(){
        return this.stop_lon;
    }

    public void set_stop_lon(String stop_lon){
        this.stop_lon = stop_lon;
    }

    public String get_zone_id(){
        return this.zone_id;
    }

    public void set_zone_id(String zone_id){
        this.zone_id = zone_id;
    }

    public String get_stop_url(){
        return this.stop_url;
    }

    public void set_stop_url(String stop_url){
        this.stop_url = stop_url;
    }

    public String get_location_type(){
        return this.location_type;
    }

    public void set_location_type(String location_type){
        this.location_type = location_type;
    }

    public String get_parent_station(){
        return this.parent_station;
    }

    public void set_parent_station(String parent_station){
        this.parent_station = parent_station;
    }
}
