package jp.techacademy.kubota.satoru.taskapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    //intent 識別
    public static final String EXTRA_TASK ="jp.techacademy.kubota.satoru.taskapp.TASK";

    //realm 関連
    private Realm realm;
    private RealmResults<Task> taskRealmResults;
    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            reloadListView();
        }
    };

    //list 関連
    private ListView mListView;
    private TaskAdapter mTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent で inputactivity へ
                Intent intent = new Intent(MainActivity.this,InputActivity.class);
                startActivity(intent);
            }
        });

        //Realm設定
        realm = Realm.getDefaultInstance();
        taskRealmResults = realm.where(Task.class).findAll();
        taskRealmResults.sort("date", Sort.DESCENDING);
        realm.addChangeListener(realmChangeListener);

        //listview 設定
        mTaskAdapter = new TaskAdapter(MainActivity.this);
        mListView = (ListView)findViewById(R.id.listView1);

        //listタップイベント
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent , View view, int position, long id) {
                //入力、編集画面に遷移
                Task task = (Task)parent.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this,InputActivity.class);
                intent.putExtra(EXTRA_TASK,task);
                startActivity(intent);
            }
        });

        //list 長押し処理
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //task 削除
                final  Task task = (Task)parent.getAdapter().getItem(position);
                //dialog show
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("削除");
                builder.setMessage(task.getTitle()+"を削除しますか？");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RealmResults<Task> results =realm.where(Task.class).equalTo("id",task.getId()).findAll();
                        realm.beginTransaction();
                        results.deleteAllFromRealm();
                        realm.commitTransaction();

                        reloadListView();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        //task がないときの処理
        if(taskRealmResults.size() ==0){
            //表示テストタスク作成
            addTaskForTest();

        }

        reloadListView();
    }

    private void reloadListView(){
        //後でtaskに変更予定

        ArrayList<Task> taskArrayList =new ArrayList<>();
        for (int i=0; i < taskRealmResults.size(); i++){
            if(!taskRealmResults.get(i).isValid()) continue;

            Task task = new Task();
            task.setId(taskRealmResults.get(i).getId());
            task.setTitle(taskRealmResults.get(i).getTitle());
            task.setContents(taskRealmResults.get(i).getContents());
            task.setDate(taskRealmResults.get(i).getDate());

            taskArrayList.add(task);
        }

        mTaskAdapter.setTaskArrayList(taskArrayList);
        mListView.setAdapter(mTaskAdapter);
        mTaskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void addTaskForTest(){
        Task task = new Task();
        task.setTitle("作業");
        task.setContents("プログラムを書いてPUSH");
        task.setDate(new Date());
        task.setId(0);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(task);
        realm.commitTransaction();
    }

}
