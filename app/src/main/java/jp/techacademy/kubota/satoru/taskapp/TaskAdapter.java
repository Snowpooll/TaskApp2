package jp.techacademy.kubota.satoru.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by snowpool on 17/01/11.
 */

public class TaskAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Task> mTaskArrayList;

    public TaskAdapter(Context context){
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTaskArrayList(ArrayList<Task> taskArrayList){
        mTaskArrayList = taskArrayList;
    }

    @Override
    public int getCount() {
        return mTaskArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return mTaskArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        if(convertview == null){
            convertview = mLayoutInflater.inflate(android.R.layout.simple_list_item_2,null);
        }
        TextView txt1 =(TextView)convertview.findViewById(android.R.id.text1);
        TextView txt2 =(TextView)convertview.findViewById(android.R.id.text2);

        //title
        txt1.setText(mTaskArrayList.get(position).getTitle());
        //date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE);
        Date date = mTaskArrayList.get(position).getDate();
        txt2.setText(simpleDateFormat.format(date));

        return convertview;
    }
}
