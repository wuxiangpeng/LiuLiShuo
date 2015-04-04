package com.github.yanglw.liulishuo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.yanglw.liulishuo.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Q3Activity extends ActionBarActivity
{
    public static final String[] URLS = new String[]{
            "http://llss.qiniudn.com/forum/image/525d1960c008906923000001_1397820588.jpg",
            "http://llss.qiniudn.com/forum/image/e8275adbeedc48fe9c13cd0efacbabdd_1397877461243.jpg",
            "http://llss.qiniudn.com/uploads/forum/topic/attached_img/5350db2ffcfff258b500dcb2/_____2014-04-18___3.52.33.png"
    };
    private int mWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q3);


        mWidth = getResources().getDisplayMetrics().widthPixels
                 - 2 * Utils.dp2px(this, 10);

        Random random = new Random();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++)
        {
            list.add(URLS[random.nextInt(3)]);
        }

        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ImageAdapter(this, list));
    }

    class ImageAdapter extends BaseAdapter
    {
        private Context mContext;
        private LayoutInflater mInflater;
        private List<String> mList;

        public ImageAdapter(Context context, List<String> list)
        {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mList = list;
        }

        @Override
        public int getCount()
        {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;
            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.i_image, parent, false);
                imageView = (ImageView) convertView.findViewById(R.id.image);

                convertView.setTag(imageView);
            }
            else
            {
                imageView = (ImageView) convertView.getTag();
            }
            Picasso.with(mContext)
                   .load(mList.get(position))
                   .error(R.drawable.a)
                   .placeholder(R.drawable.a)
                   .resize(mWidth, 0)
                   .into(imageView);
            return convertView;
        }
    }
}
