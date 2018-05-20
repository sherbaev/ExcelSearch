package io.github.sherbaev.excelsearch.Views.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.sherbaev.excelsearch.App;
import io.github.sherbaev.excelsearch.R;
import io.github.sherbaev.excelsearch.db.Pojo;
import io.github.sherbaev.excelsearch.db.PojoDao;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {
    private PojoDao dao;
    App app;
    private TextView tvName;
    private TextView tvsurName;
private  int id;
    public ItemFragment() {

    }

    public static ItemFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("ID", id);
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = (App) getActivity().getApplication();
        dao = app.getMyDataBase().loadPojoDao();
        return inflater.inflate(R.layout.fragment_item, container, false);
    }


    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id=getArguments().getInt("ID");
        Pojo pojo = dao.loadById(id);
        init(view);
        tvName.setText(pojo.name);
        tvsurName.setText(pojo.surName);
    }

    private void init(View view) {
        tvName = view.findViewById(R.id.tvName);
        tvsurName = view.findViewById(R.id.tvSurname);
    }
}
