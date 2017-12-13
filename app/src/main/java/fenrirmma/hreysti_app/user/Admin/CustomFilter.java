package fenrirmma.hreysti_app.user.Admin;

import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import fenrirmma.hreysti_app.Utils.UserHelper;

/**
 * Created by Notandi on 13.12.2017.
 */

public class CustomFilter extends Filter {

    AllUsersRecyclerAdapter adapter;
    ArrayList<UserHelper> filterList;

    public CustomFilter(AllUsersRecyclerAdapter adapter, ArrayList<UserHelper> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    //Filter
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //validity
        if(constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<UserHelper> fileteredUsers = new ArrayList<>();

            for(UserHelper uh: filterList){

                if(uh.getName().toUpperCase().contains(constraint)){
                    fileteredUsers.add(uh);
                }
            }

            results.count = fileteredUsers.size();
            results.values = fileteredUsers;

            return results;
        }

        results.count = filterList.size();
        results.values = filterList;

        return results;
    }

    // display results
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.userList = (ArrayList<UserHelper>) results.values;
        adapter.notifyDataSetChanged();


    }
}
