package com.wise.testmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wise.menu.SelectMenuView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectMenuView.SelectMenuViewDelegate {

    private SelectMenuView menuView;

    private HashMap<String, List<String>> dict = new HashMap<>();
    private List<String> keys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.text);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuView.showMenu();
            }
        });

        menuView = findViewById(R.id.menu);
        menuView.setDelegate(this);

        for (int i=0; i<10; i++) {
            List<String> list = new ArrayList<>();
            for (int j=0; j<20; j++) {
                list.add("" + i + "-" + j);
            }
            dict.put(""+i, list);
            keys.add(""+i);
        }
    }

    @Override
    public int onMenuViewSection(SelectMenuView menuView) {
        return 10;
    }

    @Override
    public int onMenuViewRow(SelectMenuView menuView, int section) {
        if (section == 0) {
            return dict.size();
        }
        else if(section == 1) {
            return dict.get(keys.get(menuView.getSelectIndexs().get(section-1))).size();
        }

        return 10;
    }

    @Override
    public String onMenuViewContent(SelectMenuView menuView, int section, int row) {

        if (section == 0 ) {
            return keys.get(row);
        }
        else if (section == 1 ) {
            return dict.get(keys.get(menuView.getSelectIndexs().get(section-1))).get(row);
        }

        return ""+section +"-"+row;
    }

    @Override
    public void onMenuViewSelect(SelectMenuView menuView, int section, int row) {

        List<Integer> selectIndexs = menuView.getSelectIndexs();

    }

    @Override
    public void onMenuViewFinish(SelectMenuView menuView, List<Integer> selectIndexs) {

    }

    @Override
    public void onMenuViewCancel(SelectMenuView menuView) {

    }
}
