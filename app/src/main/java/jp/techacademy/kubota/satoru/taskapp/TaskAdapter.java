package jp.techacademy.kubota.satoru.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by snowpool on 17/01/11.
 */

public class TaskAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mTaskArrayList;

    public TaskAdapter(Context context){
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTaskArrayList(ArrayList<String> taskArrayList){
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
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        if(convertview == null){
            convertview = mLayoutInflater.inflate(android.R.layout.simple_list_item_2,null);
        }
        TextView txt1 =(TextView)convertview.findViewById(android.R.id.text1);
        TextView txt2 =(TextView)convertview.findViewById(android.R.id.text2);

        //task から情報を取得できるように変更予定
        txt1.setText(mTaskArrayList.get(position));
        return convertview;
    }
}
