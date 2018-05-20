package io.github.sherbaev.excelsearch.Views.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import io.github.sherbaev.excelsearch.IListeners;
import io.github.sherbaev.excelsearch.R;
import io.github.sherbaev.excelsearch.Views.Fragments.ItemFragment;
import io.github.sherbaev.excelsearch.Views.Fragments.RvFragment;
import io.github.sherbaev.excelsearch.db.Pojo;

public class ListActivity extends AppCompatActivity implements IListeners {
    RvFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragment = new RvFragment();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commit();


    }

    @Override public void onClicked(Pojo pojo) {
        ItemFragment fragment=ItemFragment.newInstance(pojo.id);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
