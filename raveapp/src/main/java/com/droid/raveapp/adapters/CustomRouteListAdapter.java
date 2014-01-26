package com.droid.raveapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Filter;
import com.droid.raveapp.R;
import com.droid.raveapp.db.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 25/01/14.
 */
public class CustomRouteListAdapter extends ArrayAdapter<Route> {

    private Activity context;
    private List<Route> routeList;
    private List<Route> suggestions;

    public CustomRouteListAdapter(Activity _context, List<Route> _routeList){
        super(_context, R.layout.row_route_layout, _routeList);
        this.context = _context;
        this.routeList = _routeList;
        this.suggestions = new ArrayList<Route>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = convertView;

        if(rowView == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.row_route_layout, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.route_name = (TextView)rowView.findViewById(R.id.route_nameTXT);
            viewHolder.route_desc = (TextView)rowView.findViewById(R.id.route_descTXT);

            rowView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder)rowView.getTag();
        String route_name = routeList.get(position).get_route_short_name();
        viewHolder.route_name.setText("Route "+route_name);
        String route_desc = routeList.get(position).get_route_desc();
        viewHolder.route_desc.setText(route_desc);

        return rowView;
    }

    @Override
    public Filter getFilter(){
        return nameFilter;
    }

    Filter nameFilter = new Filter() {

        public String ResultsToString(Object object){
            String str = ((Route)(object)).get_route_short_name();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null){
                suggestions.clear();
                for(Route route: routeList){
                    if(route.get_route_short_name().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(route);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }else{
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Route> filterRoutes = (List<Route>)results.values;
            if(results != null && results.count > 0){
                clear();

                for (Route route : (List<Route>)results.values){
                    add(route);
                }
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }
        }
    };

    public static class ViewHolder{
        public TextView route_name;
        public TextView route_desc;
    }
}
