package com.github.yanglw.liulishuo;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.yanglw.liulishuo.util.Utils;


public class Q4Activity extends ActionBarActivity
{
    private boolean mFlag;
    private ImageView mImageView;
    private float mX;
    private float mY;
    private float mRawX;
    private float mRawY;
    private int mHeight;
    private int mWidth;
    private int mImageViewHeight;
    private int mImageViewWidth;

    private TouchableSpan mPressedSpan;
    private SpannableStringBuilder mSpannableStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q4);

        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = getResources().getDisplayMetrics().heightPixels;

        mImageView = (ImageView) findViewById(R.id.image);
        mImageViewHeight = Utils.dp2px(this, 100);
        mImageViewWidth = Utils.dp2px(this, 200);


        TextView textView = (TextView) findViewById(R.id.text);
        textView.setMovementMethod(new LinkTouchMovementMethod());
        mSpannableStringBuilder = getClickableSpan();
        textView.setText(mSpannableStringBuilder);
        textView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                mFlag = true;

                mPressedSpan = getPressedSpan((TextView) v,
                                              mSpannableStringBuilder,
                                              (int) mX,
                                              (int) mY);
                pressed(mSpannableStringBuilder);
                v.invalidate();

                mImageView.setVisibility(View.VISIBLE);

                showImage(v, mX, mY);
                mImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
                {
                    @Override
                    public boolean onPreDraw()
                    {
                        setImagePosition();
                        mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }

                });

                return false;
            }
        });
        textView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                TextView textView = (TextView) v;
                int action = event.getAction();
                switch (action)
                {
                    case MotionEvent.ACTION_DOWN:
                        mRawX = (int) event.getRawX();
                        mRawY = (int) event.getRawY();

                        mX = event.getX();
                        mY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mRawX = (int) event.getRawX();
                        mRawY = (int) event.getRawY();

                        if (mFlag)
                        {
                            setImagePosition();
                            showImage(v, event.getX(), event.getY());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (mFlag)
                        {
                            mImageView.setVisibility(View.INVISIBLE);
                            mFlag = false;

                            if (mPressedSpan != null)
                            {
                                Toast.makeText(Q4Activity.this,
                                               mPressedSpan.text,
                                               Toast.LENGTH_SHORT)
                                     .show();
                            }
                            textView.invalidate();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void setImagePosition()
    {
        int left = compute((int) mRawX, mImageView.getWidth(), mWidth);
        int right = left + mImageView.getWidth();
        int top = compute((int) mRawY - 100 - mImageView.getHeight(), mImageView.getHeight(), mHeight);
        int bottom = top + mImageView.getHeight();

        mImageView.layout(left, top, right, bottom);
    }

    private void showImage(View v, float x, float y)
    {
        v.setDrawingCacheEnabled(true);
        Bitmap bitmap = v.getDrawingCache(true);
        mImageView.setImageBitmap(ImageCrop(bitmap, x, y));
        v.setDrawingCacheEnabled(false);
    }

    private SpannableStringBuilder getClickableSpan()
    {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        String[] texts = new String[]{
                "the", "which", "say", "there", "man",
                "this", "than", "about", "only", "new",
                "never", "school", "most", "way", "good",
                "old", "problem", "room", "put", "god"
        };

        for (String text : texts)
        {
            SpannableString spannableInfo = new SpannableString(text);
            spannableInfo.setSpan(new TouchableSpan(text),
                                  0,
                                  spannableInfo.length(),
                                  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(spannableInfo);
            ssb.append(" ");
        }

        return ssb;
    }

    private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, int x, int y)
    {
        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();

        x += textView.getScrollX();
        y += textView.getScrollY();

        Layout layout = textView.getLayout();
        if (layout == null)
        {
            return null;
        }
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
        TouchableSpan touchedSpan = null;
        if (link.length > 0)
        {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }

    private void pressed(Spannable spannable)
    {
        if (mFlag)
        {
            mPressedSpan.setPressed(true);
            Selection.setSelection(spannable,
                                   spannable.getSpanStart(mPressedSpan),
                                   spannable.getSpanEnd(mPressedSpan));
        }
    }

    public class TouchableSpan extends ClickableSpan
    {
        private boolean mIsPressed;
        private String text;

        public TouchableSpan(String text)
        {
            this.text = text;
        }

        public void setPressed(boolean isSelected)
        {
            mIsPressed = isSelected;
        }

        @Override
        public void onClick(View widget)
        {

        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds)
        {
            super.updateDrawState(ds);
            ds.bgColor = mIsPressed ? 0xffff4444 : 0x00000000;
            ds.setUnderlineText(false);
        }
    }

    private class LinkTouchMovementMethod extends LinkMovementMethod
    {
        @Override
        public boolean onTouchEvent(@NonNull TextView textView, @NonNull Spannable spannable, @NonNull MotionEvent event)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                mPressedSpan = getPressedSpan(textView, spannable, (int) event.getX(), (int) event.getY());
                if (mPressedSpan != null)
                {
                    pressed(spannable);
                }
            }
            else
            {
                if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    TouchableSpan touchedSpan = getPressedSpan(textView, spannable, (int) event.getX(), (int) event.getY());
                    if (mPressedSpan != null && touchedSpan != mPressedSpan)
                    {
                        mPressedSpan.setPressed(false);
                        mPressedSpan = null;
                        Selection.removeSelection(spannable);
                    }
                    if (touchedSpan != null)
                    {
                        mPressedSpan = touchedSpan;
                        pressed(spannable);
                    }
                }
                else
                {
                    if (mPressedSpan != null)
                    {
                        mPressedSpan.setPressed(false);
                        super.onTouchEvent(textView, spannable, event);
                    }
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            }
            return true;
        }

    }

    public Bitmap ImageCrop(Bitmap bitmap, float x, float y)
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int cW = mImageViewWidth / 3 * 2;
        int cH = mImageViewHeight / 3 * 2;

        int retX = compute((int) x, cW, w);
        int retY = compute((int) y, cH, h);
        Matrix matrix = new Matrix();
        matrix.postScale(1.5f, 1.5f);
        return Bitmap.createBitmap(bitmap,
                                   retX,
                                   retY,
                                   cW > w ? w : cW,
                                   cH > h ? h : cH,
                                   matrix,
                                   false);
    }

    public static int compute(int position, int size, int outSize)
    {
        int retX;
        int s = size / 2;
        if (position < s)
        {
            retX = 0;
        }
        else
        {
            if (position > outSize - s)
            {
                retX = outSize - size;
            }
            else
            {
                retX = position - s;
            }
        }
        return retX;
    }
}
