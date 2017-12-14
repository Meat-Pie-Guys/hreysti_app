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
import fenrirmma.hreysti_app.Utils.UserHelper;


public class attendingUsersRecyclerAdapter extends RecyclerView.Adapter<attendingUsersRecyclerAdapter.ViewHolder> {

    private ArrayList<UserHelper> userList;
    private Activity activity;

    public attendingUsersRecyclerAdapter(Activity activity, ArrayList<UserHelper> userList){
        this.userList = userList;
        this.activity = activity;
    }

    @Override
    public attendingUsersRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_get_users_single_item, viewGroup, false);
        attendingUsersRecyclerAdapter.ViewHolder viewHolder = new attendingUsersRecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(attendingUsersRecyclerAdapter.ViewHolder viewHolder, int pos){
        viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        viewHolder.name.setText(userList.get(pos).getName());
        viewHolder.role.setText(userList.get(pos).getUserRole());
        viewHolder.ssn.setText(userList.get(pos).getSsn());
    }

    public int getItemCount(){return(null != userList ? userList.size() : 0);}


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
