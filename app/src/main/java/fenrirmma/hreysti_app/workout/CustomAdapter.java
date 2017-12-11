package fenrirmma.hreysti_app.workout;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.workout.WorkoutHelper;

/**
 * Created by Notandi on 11.12.2017.
 */

public class CustomAdapter extends BaseAdapter{

    private static ArrayList<WorkoutHelper> workoutArrayList;
    private Resources resources;
    private LayoutInflater mInflater;

    public CustomAdapter(Context context, ArrayList<WorkoutHelper> results) {
        resources = context.getResources();
        workoutArrayList = results;
        mInflater = LayoutInflater.from(context);


    }

    public int getCount() {
        return workoutArrayList.size();
    }

    public Object getItem(int position) {
        return workoutArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.workout_of_the_day_list_view, null);
            holder = new ViewHolder();
            holder.txtCount = (TextView) convertView.findViewById(R.id.count);
            holder.txtTime= (TextView) convertView.findViewById(R.id.time_of_class);
            holder.txtCoach = (TextView) convertView.findViewById(R.id.coach_of_class);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtCount.setText(String.format(resources.getString(R.string.people_registered_to_the_workout_1_s), "[5/12]"));
        holder.txtTime.setText(String.format(resources.getString(R.string.time_1_s), workoutArrayList.get(position).getDate()));
        holder.txtCoach.setText(String.format(resources.getString(R.string.coach_1_s), workoutArrayList.get(position).getCoach_id()));


        return convertView;
    }

    static class ViewHolder {
        TextView txtCount;
        TextView txtTime;
        TextView txtCoach;
    }
}