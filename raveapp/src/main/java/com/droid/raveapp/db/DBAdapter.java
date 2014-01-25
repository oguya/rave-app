package com.droid.raveapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 23/01/14.
 */
public class DBAdapter {
    private static final String DATABASE_NAME = "gtfs.sqlite";
    private static final int DATABASE_VERSION = 1;

    private Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;

    public DBAdapter(Context _context){
        this.context = _context;
        dbHelper = new DBHelper(context);
    }

    //open db
    public DBAdapter open(){
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    //get all routes & route details
    public List<Route> getAllRoutes(){
        List<Route> routesList = new ArrayList<Route>();

        Cursor cursor = db.rawQuery(Queries.GET_ALL_ROUTES, null);
        Log.e("DBAdapter", Queries.GET_ALL_ROUTES );
        if(cursor.moveToFirst()){
            do{
                Route route = new Route();

                route.set_route_id(cursor.getString(cursor.getColumnIndex(Route.KEY_ROUTE_ID)));
                route.set_trip_id(cursor.getString(cursor.getColumnIndex(Trip.KEY_TRIP_ID)));
                route.set_trip_headsign(cursor.getString(cursor.getColumnIndex(Trip.KEY_TRIP_HEADSIGN)));
                route.set_route_short_name(cursor.getString(cursor.getColumnIndex(Route.KEY_ROUTE_SHORT_NAME)));
                route.set_route_long_name(cursor.getString(cursor.getColumnIndex(Route.KEY_ROUTE_LONG_NAME)));
                route.set_route_desc(cursor.getString(cursor.getColumnIndex(Route.KEY_ROUTE_DESC)));

                routesList.add(route);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return routesList;
    }

    //get stages given route no
    public List<Stop> getStops(String route_name){
        List<Stop> stopsList = new ArrayList<Stop>();
        String sql = Queries.GET_SPECIFIC_STAGE+" "+route_name+" )";
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                Stop stop = new Stop();

                stop.set_stop_name(cursor.getString(cursor.getColumnIndex(Stop.KEY_STOP_NAME)));
                stop.set_stop_lat(cursor.getString(cursor.getColumnIndex(Stop.KEY_STOP_LAT)));
                stop.set_stop_lon(cursor.getString(cursor.getColumnIndex(Stop.KEY_STOP_LON)));

                stopsList.add(stop);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return stopsList;
    }



    public static class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private static class Queries{
        public static  final String GET_ALL_ROUTES =
                "SELECT trips.route_id AS "+Route.KEY_ROUTE_ID+", "+
                        "trips.trip_id AS "+Trip.KEY_TRIP_ID+", "+
                        "trips.trip_headsign AS "+Trip.KEY_TRIP_HEADSIGN+", "+
                        "routes.route_short_name AS "+Route.KEY_ROUTE_SHORT_NAME+", "+
                        "routes.route_long_name AS "+Route.KEY_ROUTE_LONG_NAME+", "+
                        "routes.route_desc AS "+Route.KEY_ROUTE_DESC+" "+
                "FROM trips INNER JOIN routes ON trips.route_id = routes.route_id " +
                "GROUP BY routes.route_short_name ORDER BY routes.route_short_name ASC";


        public static final String GET_SPECIFIC_STAGE =
                "SELECT stop_times.trip_id AS "+Trip.KEY_TRIP_ID+", "+
                        "stop_times.stop_id AS "+StopTime.KEY_STOP_ID+", "+
                        "stops.stop_name AS "+Stop.KEY_STOP_NAME+", "+
                        "stops.stop_desc AS "+Stop.KEY_STOP_DESC+", "+
                        "stops.stop_lat AS "+Stop.KEY_STOP_LAT+", "+
                        "stops.stop_lon AS "+Stop.KEY_STOP_LON+", "+
                "FROM stop_times  INNER JOIN stops ON stops.stop_id = stop_times.stop_id" +
                "INNER JOIN trips ON stop_times.trip_id = trips.trip_id " +
                "WHERE stop_times.trip_id IN " +
                        "(SELECT trips.trip_id from routes " +
                        "INNER JOIN trips on trips.route_id = routes.route_id" +
                        " WHERE route_short_name=";
    }
}
