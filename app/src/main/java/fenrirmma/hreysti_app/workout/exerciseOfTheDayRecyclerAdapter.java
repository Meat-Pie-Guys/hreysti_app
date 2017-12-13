package fenrirmma.hreysti_app.workout;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.WorkoutHelper;

/**
 * Created by magnu on 13/12/2017.
 */

public class exerciseOfTheDayRecyclerAdapter extends RecyclerView.Adapter<exerciseOfTheDayRecyclerAdapter.ViewHolder>{

    private ArrayList<WorkoutHelper> exerciseList;
    private Activity activity;

    public exerciseOfTheDayRecyclerAdapter(Activity activity, ArrayList<WorkoutHelper> exerciseList){
        this.exerciseList = exerciseList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_get_exercise_single_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(exerciseOfTheDayRecyclerAdapter.ViewHolder viewHolder, int pos){
        viewHolder.image_exercise.setImageResource(R.mipmap.ic_launcher);
        viewHolder.workout_day.setText(exerciseList.get(pos).getDate());
        viewHolder.display_time.setText(exerciseList.get(pos).getTime());
        viewHolder.today_coach.setText(exerciseList.get(pos).getCoach_name());
    }

    public int getItemCount(){return(null != exerciseList ? exerciseList.size() : 0);}

    protected class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_exercise;
        TextView excercise;
        TextView workout_day;
        TextView display_time;
        TextView today_coach;

        public ViewHolder(View view){
            super(view);
            image_exercise = view.findViewById(R.id.image_exercise);
            workout_day = view.findViewById(R.id.workout_day);
            display_time = view.findViewById(R.id.display_time);
            today_coach = view.findViewById(R.id.today_coach);
        }
    }
}
