package io.github.sherbaev.excelsearch.Views.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.github.sherbaev.excelsearch.App;
import io.github.sherbaev.excelsearch.db.Pojo;
import io.github.sherbaev.excelsearch.R;
import io.github.sherbaev.excelsearch.adapters.RvAdapter;
import io.github.sherbaev.excelsearch.db.PojoDao;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class RvFragment extends Fragment {
    private ArrayList<Pojo> data;
    private RvAdapter adapter;
    private RecyclerView recyclerView;
    private PojoDao dao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rv, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        App app = (App) getActivity().getApplication();
        dao = app.getMyDataBase().loadPojoDao();
        dao.loadAll();
        recyclerView = view.findViewById(R.id.mRvList);
//        data=new ArrayList<>();
        adapter = new RvAdapter();
        recyclerView.setAdapter(adapter);
        Disposable d = dao.loadAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(todos -> adapter.submitList(todos));
    }
}
