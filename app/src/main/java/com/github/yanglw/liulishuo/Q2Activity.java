package com.github.yanglw.liulishuo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;


public class Q2Activity extends ActionBarActivity
{
    public static final int TIMES = 3;
    private int mTime;
    private TextView mTextView;
    private AnimatorSet mAnimatorSet;
    private ViewGroup mViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q2);
    }

    private void initAnimator()
    {
        if (mAnimatorSet == null)
        {
            mTextView = (TextView) findViewById(R.id.text);
            final ImageView imageView = (ImageView) findViewById(R.id.image);
            mViewGroup = (ViewGroup) findViewById(R.id.layout);

            PropertyValuesHolder pvhX1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
            PropertyValuesHolder pvhY1 = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
            PropertyValuesHolder pvhAlpha1 = PropertyValuesHolder.ofFloat("alpha", 1f, 1f);
            ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(imageView, pvhX1, pvhY1, pvhAlpha1);
            animator1.setDuration(1000);
            animator1.setRepeatCount(2);
            animator1.setInterpolator(new AccelerateInterpolator());

            animator1.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationRepeat(Animator animation)
                {
                    mTime--;
                    mTextView.setText(String.valueOf(mTime));
                }
            });

            PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f);
            PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f);
            PropertyValuesHolder pvhAlpha2 = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
            ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(imageView, pvhX2, pvhY2, pvhAlpha2);
            animator2.setDuration(500);
            animator2.setInterpolator(new AccelerateInterpolator());
            animator2.addListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mViewGroup.setVisibility(View.INVISIBLE);
                }
            });

            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.play(animator1).before(animator2);
        }
    }

    private void start()
    {
        initAnimator();

        if (mAnimatorSet.isStarted())
        {
            mAnimatorSet.cancel();
        }

        if (mViewGroup.getVisibility() != View.VISIBLE)
        {
            mViewGroup.setVisibility(View.VISIBLE);
        }

        mTime = TIMES;
        mTextView.setText(String.valueOf(mTime));
        mAnimatorSet.start();
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
            start();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
