package io.github.sherbaev.excelsearch.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Pojo {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String surName;

//    public Pojo(String name, String surName) {
//        this.name = name;
//        this.surName = surName;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getSurName() {
//        return surName;
//    }
//
//    public void setSurName(String surName) {
//        this.surName = surName;
//    }
}
