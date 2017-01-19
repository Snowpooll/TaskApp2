package jp.techacademy.kubota.satoru.taskapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by snowpool on 17/01/15.
 */

public class TaskApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
