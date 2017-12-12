package fenrirmma.hreysti_app.user.Admin;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fenrirmma.hreysti_app.R;

/**
 * Created by magnu on 10/12/2017.
 */

public class AllUsersRecyclerAdapter extends RecyclerView.Adapter<AllUsersRecyclerAdapter.ViewHolder> {

    private List<UserHelper> userList;
    private Activity activity;

    public AllUsersRecyclerAdapter(Activity activity, List<UserHelper> userList){
        this.userList = userList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_get_users_single_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AllUsersRecyclerAdapter.ViewHolder viewHolder, int pos){
        viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        viewHolder.name.setText(userList.get(pos).getName());
        viewHolder.role.setText(userList.get(pos).getUserRole());
        viewHolder.ssn.setText(userList.get(pos).getSsn());
        viewHolder.expiration.setText(userList.get(pos).getExpireDate());
    }

    public int getItemCount(){return(null != userList ? userList.size() : 0);}

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView name;
        private TextView role;
        private TextView ssn;
        private TextView expiration;
        private View container;

        public ViewHolder(View view){
            super(view);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            role = view.findViewById(R.id.role);
            ssn = view.findViewById(R.id.ssn);
            expiration = view.findViewById(R.id.expiration);
            container = view.findViewById(R.id.card_view);
        }
    }
}

