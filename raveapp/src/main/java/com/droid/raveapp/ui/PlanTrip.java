package com.droid.raveapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

    private Button submit_BTN;
    private AutoCompleteTextView route_TXT;
    private AutoCompleteTextView stage_TXT;

    private String selected_route;
    private String selected_stage;

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
        routeList = dbAdapter.getAllRoutes();

        route_TXT = (AutoCompleteTextView)findViewById(R.id.route_name_TXT);
        route_TXT.setOnItemClickListener(routes_spinner_listener);
        route_TXT.setAdapter(new CustomRouteListAdapter(PlanTrip.this, routeList));
        stage_TXT = (AutoCompleteTextView)findViewById(R.id.stage_TXT);
        stage_TXT.setOnItemClickListener(stage_spinner_listener);


    }


    private AdapterView.OnItemClickListener routes_spinner_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Route route = routeList.get(position);
            String details = "";
            details += "trip_ID: "+route.get_trip_id();
            details += " trip headsign "+route.get_trip_headsign();
            details += " route long name: "+route.get_route_long_name();

            //load stages for selected route
            loadStages(route.get_route_short_name());
        }
    };

    public void loadStages(String route_short_name){
        stagesList = dbAdapter.getStops(route_short_name);
        stage_TXT.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, slave.get_stop_names(stagesList)));
        stage_TXT.setEnabled(true);
    }

    private AdapterView.OnItemClickListener stage_spinner_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

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

        public boolean processForm(){
            return false;
        }

    }

}
