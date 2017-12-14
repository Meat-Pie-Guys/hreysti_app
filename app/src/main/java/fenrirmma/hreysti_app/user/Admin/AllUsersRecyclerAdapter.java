package fenrirmma.hreysti_app.user.Admin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.UserHelper;

public class AllUsersRecyclerAdapter extends RecyclerView.Adapter<AllUsersRecyclerAdapter.ViewHolder> implements Filterable{


    ArrayList<UserHelper> userList;
    private ArrayList<UserHelper> filterList;
    private Activity activity;
    CustomFilter filter;

    public AllUsersRecyclerAdapter(Activity activity, ArrayList<UserHelper> userList){
        this.userList = userList;
        this.filterList = userList;
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

        viewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), userInfoAdminActivity.class);
            intent.putExtra("NAME", userList.get(pos).getName());
            intent.putExtra("SSN", userList.get(pos).getSsn());
            intent.putExtra("OPENID", userList.get(pos).getOpenId());
            intent.putExtra("ROLE", userList.get(pos).getUserRole());
            intent.putExtra("STARTDATE", userList.get(pos).getStartDate());
            intent.putExtra("EXPIREDATE", userList.get(pos).getExpireDate());
            view.getContext().startActivity(intent);
            activity.finish();
        });
    }

    public int getItemCount(){return(null != userList ? userList.size() : 0);}

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CustomFilter(this, filterList);
        }
        return filter;
    }

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

