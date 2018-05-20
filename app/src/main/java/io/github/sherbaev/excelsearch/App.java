package io.github.sherbaev.excelsearch;

import android.app.Application;
import android.arch.persistence.room.Room;

import io.github.sherbaev.excelsearch.db.MyDataBase;

public class App extends Application {
    private MyDataBase myDataBase;

    @Override public void onCreate() {
        super.onCreate();
        myDataBase = Room.databaseBuilder(this,
                MyDataBase.class, "database1")
                .allowMainThreadQueries()
                .build();
    }

    public MyDataBase getMyDataBase() {
        return myDataBase;
    }
}
