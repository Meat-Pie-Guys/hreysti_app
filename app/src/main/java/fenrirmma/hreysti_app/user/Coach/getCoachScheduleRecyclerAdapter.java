package fenrirmma.hreysti_app.user.Coach;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.WorkoutHelper;
import fenrirmma.hreysti_app.workout.updateWorkoutActivity;

/**
 * Created by magnu on 13/12/2017.
 */

public class getCoachScheduleRecyclerAdapter extends RecyclerView.Adapter<getCoachScheduleRecyclerAdapter.ViewHolder> {

    private ArrayList<WorkoutHelper> list;
    private Activity activity;

    public getCoachScheduleRecyclerAdapter(Activity activity, ArrayList<WorkoutHelper> list){
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_get_coach_single_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(getCoachScheduleRecyclerAdapter.ViewHolder viewHolder, int pos){
        viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        viewHolder.workout_day.setText(list.get(pos).getDate());
        viewHolder.display_time.setText(list.get(pos).getTime());
        viewHolder.today_coach.setText(list.get(pos).getCoach_name());

        viewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), updateWorkoutActivity.class);
            intent.putExtra("id", list.get(pos).getOpen_id());
            intent.putExtra("coach_name", list.get(pos).getCoach_name());
            intent.putExtra("description", list.get(pos).getDescription());
            intent.putExtra("date", list.get(pos).getDate());
            intent.putExtra("time", list.get(pos).getTime());
            view.getContext().startActivity(intent);
        });
    }


    public int getItemCount(){return(null != list ? list.size() : 0);}

    protected class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView workout_day;
        TextView display_time;
        TextView today_coach;

        public ViewHolder(View view){
            super(view);
            image = view.findViewById(R.id.image);
            workout_day = view.findViewById(R.id.workout_day);
            display_time = view.findViewById(R.id.display_time);
            today_coach = view.findViewById(R.id.today_coach);
        }
    }
}
