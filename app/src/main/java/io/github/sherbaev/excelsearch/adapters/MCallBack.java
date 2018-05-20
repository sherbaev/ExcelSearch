package io.github.sherbaev.excelsearch.adapters;

import android.support.v7.util.DiffUtil;

import io.github.sherbaev.excelsearch.db.Pojo;

public class MCallBack extends DiffUtil.ItemCallback<Pojo> {

    @Override public boolean areItemsTheSame(Pojo oldItem, Pojo newItem) {
        return oldItem.name == newItem.name;
    }

    @Override public boolean areContentsTheSame(Pojo oldItem, Pojo newItem) {
        return oldItem.equals(newItem);
    }
}
