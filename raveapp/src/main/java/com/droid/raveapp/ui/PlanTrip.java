package com.droid.raveapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.droid.raveapp.R;
import com.droid.raveapp.adapters.CustomRouteListAdapter;
import com.droid.raveapp.db.DBAdapter;
import com.droid.raveapp.db.Route;
import com.droid.raveapp.db.Stop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 25/01/14.
 */
public class PlanTrip extends ActionBarActivity {

    private final String APP_TAG = "PlanTrip";
    private Button submit_BTN;
    private AutoCompleteTextView route_TXT;
    private AutoCompleteTextView stage_TXT;
    private Spinner reminder_spinner;
    private Spinner departure_spinner;

    private String selected_route;
    private String selected_stage;
    private String departure;
    private String reminder_interval;

    private DBAdapter dbAdapter;
    private List<Stop> stagesList;
    private List<Route> routeList;
    private Slave slave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_trip);

        //load views
        slave = new Slave(this);
        dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        routeList = dbAdapter.getAllRoutes();

        route_TXT = (AutoCompleteTextView)findViewById(R.id.route_name_TXT);
        route_TXT.setOnItemClickListener(routes_spinner_listener);
//        route_TXT.setAdapter(new CustomRouteListAdapter(PlanTrip.this, routeList));
        route_TXT.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, slave.get_route_names(routeList) ));
        stage_TXT = (AutoCompleteTextView)findViewById(R.id.stage_TXT);
        stage_TXT.setOnItemClickListener(stage_spinner_listener);

        reminder_spinner = (Spinner)findViewById(R.id.reminder_spinner);
        departure_spinner = (Spinner)findViewById(R.id.depart_spinner);

    }

    //save stuff
    @Override
    public void onPause(){
        dbAdapter.close();
        super.onPause();
    }

    @Override
    public void onResume(){
        dbAdapter.open();
        super.onResume();
    }

    private AdapterView.OnItemClickListener routes_spinner_listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selected_route = parent.getItemAtPosition(position).toString();
            String routeNo = route_TXT.getText().toString();
            route_TXT.setText(routeNo);
            //load stages for selected route
            loadStages(routeNo);
        }
    };

    private AdapterView.OnItemClickListener stage_spinner_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selected_stage = parent.getItemAtPosition(position).toString();
        }
    };

    private AdapterView.OnItemSelectedListener reminder_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            reminder_interval = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener depart_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            departure = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void loadStages(String route_short_name){
        stagesList = dbAdapter.getStops(route_short_name);
        stage_TXT.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, slave.get_stop_names(stagesList)));
        stage_TXT.setEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.plan_trip_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_save:
                Toast.makeText(this, "Saving...", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean processForm(View view){
        String route_no = route_TXT.getText().toString();
        String stage_name = stage_TXT.getText().toString();

        List<Route> routes = slave.search_route(route_no);
        List<Stop> stops = slave.search_stage(stage_name);

        String details = "";

        for(Route rt : routes){
            details += "trip_ID: "+rt.get_trip_id();
            details += " trip headsign "+rt.get_trip_headsign();
            details += " route long name: "+rt.get_route_long_name();
            Log.e(APP_TAG, details);
            Toast.makeText(getApplicationContext(), details, Toast.LENGTH_SHORT).show();
        }
        details="";
        for(Stop sp : stops){
            details += "trip_ID: "+sp.get_stop_id();
            details += "name:"+sp.get_stop_name();
            details += "lat: "+sp.get_stop_lat();
            details += "lat: "+sp.get_stop_lon();
            Log.e(APP_TAG, details);
            Toast.makeText(getApplicationContext(), details, Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    //perform simple tasks..unclutter onCreate()
    public class Slave{
        public Context context;

        public Slave(Context context){
            this.context = context;
        }

        public List<String> get_route_names(List<Route> routes){
            ArrayList<String> route_names = new ArrayList<String>();
            for (Route route : routes){
                route_names.add(route.get_route_short_name());
            }
            return route_names;
        }

        public List<String> get_stop_names(List<Stop> stops){
            ArrayList<String> stop_names = new ArrayList<String>();
            for(Stop stop : stops){
                stop_names.add(stop.get_stop_name());
            }
            return stop_names;
        }

        public List<Route> search_route(String route_name){
            List<Route> route = new ArrayList<Route>();
            for(Route rt : routeList){
                if(rt.get_route_short_name().equalsIgnoreCase(route_name))
                    route.add(rt);
            }
            return route;
        }

        public List<Stop> search_stage(String stage_name){
            List<Stop> stop = new ArrayList<Stop>();
            for(Stop sp : stagesList){
                if(sp.get_stop_name().equalsIgnoreCase(stage_name))
                    stop.add(sp);
            }
            return stop;
        }

    }

}
