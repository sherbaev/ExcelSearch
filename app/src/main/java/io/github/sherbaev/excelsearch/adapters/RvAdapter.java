package io.github.sherbaev.excelsearch.adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.sherbaev.excelsearch.IListeners;
import io.github.sherbaev.excelsearch.db.Pojo;
import io.github.sherbaev.excelsearch.R;

public class RvAdapter extends ListAdapter<Pojo,RvAdapter.ViewHolder> {
private IListeners listeners;
    public RvAdapter() {
        super(new MCallBack());
    }


    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            listeners= (IListeners) itemView.getContext();

        }

        public void onBind(Pojo pojo) {
            tvName.setText(pojo.name);
            itemView.setOnClickListener(v-> listeners.onClicked(pojo));
        }
    }
}
