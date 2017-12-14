package fenrirmma.hreysti_app.workout;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.SessionAccess;
import fenrirmma.hreysti_app.Utils.WorkoutHelper;

/**
 * Created by magnu on 13/12/2017.
 */

public class exerciseOfTheDayRecyclerAdapter extends RecyclerView.Adapter<exerciseOfTheDayRecyclerAdapter.ViewHolder> {

    private ArrayList<WorkoutHelper> exerciseList;
    private Activity activity;
    private SessionAccess sa;



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
        viewHolder.attending.setText("Attending: " + exerciseList.get(pos).getAttending());


        viewHolder.itemView.setOnClickListener(view -> {

            sa = SessionAccess.getInstance(view.getContext());

            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

            if(sa.getRole().equals("Client")) try {
                Date dateGot = formatter.parse(exerciseList.get(pos).getDate());
                Date now = new Date();
                if (now.before(dateGot)) {

                    Ion.with(view.getContext())
                            .load("GET", "http://10.0.2.2:5000/workout/" + exerciseList.get(pos).getOpen_id())
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("fenrir-token", sa.getToken())
                            .setTimeout(1000)
                            .asJsonObject()
                            .setCallback((e, result) -> {
                                if(e != null){
                                    Toast.makeText(view.getContext(), "ION ERROR not cock", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                                }else {
                                    int code = result.get("error").getAsInt();
                                    if (code != 0) {
                                        Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                                    }
                                    else{
                                        Toast.makeText(view.getContext(), "ATTENDED WORKOUT", Toast.LENGTH_SHORT).show(); //TODO breyta í eitthvað meira hot
                                    }
                                }
                            });
                    exerciseList.notify();

                } else {
                    // TODO láta user vita að þetta var ekkihægt því þetta var gömul dagsetning?
                }
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }
            else{
            }
        });
    }

    public int getItemCount(){return(null != exerciseList ? exerciseList.size() : 0);}

    protected class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_exercise;
        TextView attending;
        TextView workout_day;
        TextView display_time;
        TextView today_coach;

        public ViewHolder(View view){
            super(view);
            image_exercise = view.findViewById(R.id.image_exercise);
            workout_day = view.findViewById(R.id.workout_day);
            display_time = view.findViewById(R.id.display_time);
            today_coach = view.findViewById(R.id.today_coach);
            attending = view.findViewById(R.id.attending);
        }
    }
}
