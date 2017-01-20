package jp.techacademy.kubota.satoru.taskapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import io.realm.Realm;
import io.realm.RealmResults;

public class InputActivity extends AppCompatActivity {

    private int mYear,mMonth,mDay,mHour,mMinute;
    private Button Datebtn, Timebtn;
    private EditText titleEdit,contentEdit;
    private Task mTask;

    private View.OnClickListener mOnDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(InputActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            mYear = year;
                            mMonth = month;
                            mDay = day;
                            String dateString = mYear+"/"+String.format("%02d",(mMonth+1))+"/"+String.format("%02d",mDay);
                            Datebtn.setText(dateString);
                        }
                    },mYear,mMonth,mDay);
            datePickerDialog.show();
        }
    };

    private View.OnClickListener mOnTImeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(InputActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hour, int minute) {
                    mHour = hour;
                    mMinute = minute;
                    String timeString = String.format("%02d",mHour)+":"+String.format("%02d",mMinute);
                    Timebtn.setText(timeString);
                }
            },mHour,mMinute,false);
            timePickerDialog.show();
        }
    };

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addTask();
            finish();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        //actionbar setting
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //UI setting
        Datebtn =(Button)findViewById(R.id.date_btn);
        Datebtn.setOnClickListener(mOnDateClickListener);
        Timebtn =(Button)findViewById(R.id.times_btn);
        Timebtn.setOnClickListener(mOnTImeClickListener);
        findViewById(R.id.done_btn).setOnClickListener(mOnDoneClickListener);
        titleEdit =(EditText)findViewById(R.id.title_edit);
        contentEdit =(EditText)findViewById(R.id.content_edit);

        //EXTRA_TASK から id 取得　id から task　インスタンス取得
        Intent intent = getIntent();
        mTask = (Task) intent.getSerializableExtra(MainActivity.EXTRA_TASK);
//        int taskId = intent.getIntExtra(MainActivity.EXTRA_TASK,-1);
//        Realm realm = Realm.getDefaultInstance();
//        mTask = realm.where(Task.class).equalTo("id",taskId).findFirst();
//        realm.close();

        //新規作成の場合
        if(mTask == null){
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
        }
        //更新処理
        else{
            titleEdit.setText(mTask.getTitle());
            contentEdit.setText(mTask.getContents());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mTask.getDate());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);

            String dateString = mYear +"/"+String.format("%02d",(mMonth+1))+"/"+String.format("%02d",mDay);
            String timeString = String.format("%02d",mHour)+":"+String.format("%02d",mMinute);
            Datebtn.setText(dateString);
            Timebtn.setText(timeString);
        }
    }

    private void addTask(){
        Realm realm = Realm.getDefaultInstance();

        if(mTask ==null){
            //新規作成の場合
            mTask = new Task();
            RealmResults<Task> taskRealmResults = realm.where(Task.class).findAll();

            int identifier;
            if(taskRealmResults.max("id") != null){
                identifier = taskRealmResults.max("id").intValue()+1;
            }else {
                identifier=0;
            }
            mTask.setId(identifier);
        }

        String title = titleEdit.getText().toString();
        String content = contentEdit.getText().toString();

        mTask.setTitle(title);
        mTask.setContents(content);

        GregorianCalendar calendar = new GregorianCalendar(mYear,mMonth,mDay,mHour,mMinute);
        Date date = calendar.getTime();
        mTask.setDate(date);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(mTask);
        realm.commitTransaction();

        realm.close();

        //pending intent setting
        Intent resultIntent = new Intent(getApplicationContext(),TaskAlarmReceiver.class);
        resultIntent.putExtra(MainActivity.EXTRA_TASK,mTask.getId());
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                this,
                mTask.getId(),
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        //alarm manager setting
        AlarmManager alarmManager =(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),resultPendingIntent);
    }
}
