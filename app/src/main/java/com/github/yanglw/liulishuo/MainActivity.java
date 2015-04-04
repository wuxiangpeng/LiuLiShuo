package com.github.yanglw.liulishuo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity
{
    private Class[] mCls = new Class[]{
            Q1Activity.class,
            Q2Activity.class,
            Q3Activity.class,
            Q4Activity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listview);

        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < 5; i++)
        {
            list.add(String.valueOf(i));
        }
        listView.setAdapter(new ArrayAdapter<>(this,
                                               android.R.layout.simple_expandable_list_item_1,
                                               list));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                startActivity(new Intent(MainActivity.this,
                                         mCls[position]));
            }
        });
    }
}
