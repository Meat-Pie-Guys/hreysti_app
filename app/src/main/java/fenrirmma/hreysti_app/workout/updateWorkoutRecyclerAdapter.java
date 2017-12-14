package fenrirmma.hreysti_app.workout;

import android.app.Activity;
import android.app.ActivityOptions;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.UserHelper;

/**
 * Created by magnu on 14/12/2017.
 */

public class updateWorkoutRecyclerAdapter extends RecyclerView.Adapter<updateWorkoutRecyclerAdapter.ViewHolder> {

    ArrayList<UserHelper> list;
    private Activity activity;
    private TextView coach_name;
    private updateWorkoutActivity updateWorkoutActivity;

    public updateWorkoutRecyclerAdapter(Activity activity, ArrayList<UserHelper> list){
        this.activity = activity;
        this.list = list;
        coach_name = activity.findViewById(R.id.coach_name);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_get_users_single_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(updateWorkoutRecyclerAdapter.ViewHolder viewHolder, int pos){
        viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        viewHolder.name.setText(list.get(pos).getName());
        viewHolder.role.setText(list.get(pos).getUserRole());
        viewHolder.ssn.setText(list.get(pos).getSsn());

        viewHolder.itemView.setOnClickListener(view -> {
            coach_name.setText(list.get(pos).getName());
        });
    }

    public int getItemCount(){return(null != list ? list.size() : 0);}

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView name;
        private TextView role;
        private TextView ssn;

        public ViewHolder(View view){
            super(view);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            role = view.findViewById(R.id.role);
            ssn = view.findViewById(R.id.ssn);
        }
    }

}
