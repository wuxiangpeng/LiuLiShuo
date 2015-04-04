package com.github.yanglw.liulishuo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.yanglw.liulishuo.util.HttpManager;

import org.json.JSONException;
import org.json.JSONObject;


public class Q1Activity extends ActionBarActivity
{
    private static final String URL = "http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId=101010100&imei=529e2dd3d767bdd3595eec30dd481050&device=pisces&miuiVersion=JXCCNBD20.0&modDevice=&source=miuiWeatherApp";
    private TextView mTextView;
    private Response.ErrorListener mErrorListener;
    private Response.Listener<JSONObject> mListener;
    private ProgressBar mProgressBar;
    private boolean mRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q1);

        mTextView = (TextView) findViewById(R.id.text);

        mProgressBar = (ProgressBar) findViewById(R.id.progress);

        request();
    }

    private void request()
    {
        if (mRunning)
        {
            return;
        }
        mRunning = true;

        if (mErrorListener == null)
        {
            mErrorListener = new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    questFinish();
                    Toast.makeText(Q1Activity.this,
                                   R.string.err_request,
                                   Toast.LENGTH_SHORT)
                         .show();
                }
            };
        }

        if (mListener == null)
        {
            mListener = new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    questFinish();
                    try
                    {
                        String city = response.getJSONObject("forecast").getString("city");
                        String weather = response.getJSONObject("realtime").getString("weather");

                        mTextView.setText(getString(R.string.weather_format,
                                                    city,
                                                    weather));
                    }
                    catch (JSONException e)
                    {
                        mTextView.setText(R.string.err_parser);
                    }
                }
            };
        }

        Request request = new JsonObjectRequest(
                URL,
                mListener,
                mErrorListener);

        request.setShouldCache(true);
        HttpManager.add(request);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void questFinish()
    {
        mRunning = false;
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            request();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
