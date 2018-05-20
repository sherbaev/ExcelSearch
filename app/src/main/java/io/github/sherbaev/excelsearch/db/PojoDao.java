package io.github.sherbaev.excelsearch.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface PojoDao {
    @Insert void addPojo(Pojo pojo);

    @Query("select * from Pojo") Flowable<List<Pojo>> loadAll();

    @Query("select * from Pojo where id=:id")Pojo loadById(int id);
}
