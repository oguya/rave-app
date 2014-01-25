package com.droid.raveapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.droid.raveapp.R;
import com.droid.raveapp.utils.DBUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class MainActivity extends ActionBarActivity {

    private final String APP_TAG = "MainActivity";
    private final String PREF_DB_COPIED = "DBCopied";
    private final String DB_NAME = "gtfs.sqlite";

    private LoadStuff loadStuff;
    private SharedPreferences prefs;
    private String EXTERNAL_DB_DIR = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadStuff loadStuff = new LoadStuff(this);
        prefs = getPreferences(MODE_PRIVATE);
        prefs.getBoolean(PREF_DB_COPIED, false);

        //copy db
        try{
            EXTERNAL_DB_DIR = getApplicationInfo().dataDir+ File.pathSeparator;
        }catch (NullPointerException npe){
            EXTERNAL_DB_DIR = "/data/data/"+getPackageName()+"/databases/";
        }
        String DBPath = getDatabasePath(DB_NAME).getAbsolutePath();

        if(!loadStuff.DBExists(DBPath)){
            loadStuff.loadDB(DBPath);
            prefs.edit().putBoolean(PREF_DB_COPIED, true).commit();
            Log.i(APP_TAG, "copying db to " + DBPath);
        }else{
            Log.i(APP_TAG,"db already copied to:"+DBPath);
        }

    }

    //handle main click events
    public void onClick(View view){
        switch (view.getId()){
            case R.id.plan_trip_BTN:
                startActivity(new Intent(this, PlanTrip.class));
                break;
            case R.id.view_stats:
                startActivity(new Intent(this, ViewStats.class));
                break;
            case R.id.search_BTN:
                startActivity(new Intent(this, SearchStage.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class LoadStuff implements Serializable {
        private Context context;

        public LoadStuff(Context context){
            this.context = context;
        }

        public boolean DBExists(String DBPath){
            File db = new File(DBPath);
            return (db.exists() && db.isFile());
        }

        public void loadDB(String DBPath){
            DBUtils dbUtils = new DBUtils();
            try{
                dbUtils.copyDB(getBaseContext().getResources().openRawResource(R.raw.gtfs_testing),
                        new FileOutputStream(DBPath));
            }catch (IOException e){
                Log.e(APP_TAG, "Error while copying DB!\n "+e.getMessage());
            }

        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
