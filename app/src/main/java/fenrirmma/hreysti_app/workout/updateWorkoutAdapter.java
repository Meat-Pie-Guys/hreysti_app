package fenrirmma.hreysti_app.workout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fenrirmma.hreysti_app.R;
import fenrirmma.hreysti_app.Utils.UserHelper;
import fenrirmma.hreysti_app.Utils.WorkoutHelper;

/**
 * Created by Notandi on 11.12.2017.
 */

public class updateWorkoutAdapter extends BaseAdapter{

    private static ArrayList<UserHelper> userHelperArrayList;
    private Resources resources;
    private LayoutInflater mInflater;

    public updateWorkoutAdapter(Context context, ArrayList<UserHelper> results) {
        resources = context.getResources();
        userHelperArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return userHelperArrayList.size();
    }

    public Object getItem(int position) {
        return userHelperArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_get_users_single_item, null);
            holder = new ViewHolder();
            holder.role= convertView.findViewById(R.id.role);
            holder.name = convertView.findViewById(R.id.name);
            holder.ssn = convertView.findViewById(R.id.ssn);
            holder.image = convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setImageResource(R.mipmap.ic_launcher);
        holder.role.setText(String.format(resources.getString(R.string._1_s), userHelperArrayList.get(position).getUserRole()));
        holder.ssn.setText(String.format(resources.getString(R.string.admin_user_ssn), userHelperArrayList.get(position).getSsn()));
        holder.name.setText(String.format(resources.getString(R.string.admin_user_name), userHelperArrayList.get(position).getName()));

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView role;
        TextView ssn;
        ImageView image;
    }
}
