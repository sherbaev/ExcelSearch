package io.github.sherbaev.excelsearch.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = Pojo.class,version = 1)
public abstract class MyDataBase extends RoomDatabase{
   public abstract PojoDao loadPojoDao();
}
