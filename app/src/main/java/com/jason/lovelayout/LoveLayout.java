package com.jason.lovelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class LoveLayout extends RelativeLayout {

    private int[] imageResIds = {
            R.drawable.heart0,
            R.drawable.heart1,
            R.drawable.heart2,
            R.drawable.heart3,
            R.drawable.heart4,
            R.drawable.heart5,
            R.drawable.heart6,
            R.drawable.heart7,
            R.drawable.heart8};

    private AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
    private int mWidth;
    private int mHeight;
    private int mDrawableWidth;
    private int mDrawableHeight;
    private Random mRandom;
    private LayoutParams layoutParams;

    public LoveLayout(Context context) {
        this(context, null);
    }

    public LoveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRandom = new Random();
        Drawable drawable = ContextCompat.getDrawable(context, imageResIds[0]);
        mDrawableWidth = drawable.getIntrinsicWidth();
        mDrawableHeight = drawable.getIntrinsicHeight();
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(CENTER_HORIZONTAL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }


    public void addLove() {
        final ImageView loveIv = new ImageView(getContext());
        loveIv.setLayoutParams(layoutParams);
        addView(loveIv);
        loveIv.setImageResource(imageResIds[mRandom.nextInt(imageResIds.length - 1)]);
        AnimatorSet animatorSet = initAnimation(loveIv);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(loveIv);
            }
        });
        animatorSet.start();
    }

    private AnimatorSet initAnimation(ImageView loveIv) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(loveIv, "alpha", 0.3f, 1f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(loveIv, "scaleX", 0.3f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(loveIv, "scaleY", 0.3f, 1f);

        AnimatorSet innerAnimatorSet = new AnimatorSet();
        innerAnimatorSet.setDuration(350);
        innerAnimatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);

        animatorSet.playSequentially(innerAnimatorSet, getBezierAnimator(loveIv));
        animatorSet.setTarget(loveIv);
        return animatorSet;
    }

    private Animator getBezierAnimator(final ImageView loveIv) {
        PointF startPoint = new PointF(mWidth / 2 - mDrawableWidth / 2, mHeight - mDrawableHeight);
        PointF controlPoint1 = new PointF(mRandom.nextInt(mWidth), mHeight - mRandom.nextInt(mHeight / 2));
        PointF controlPoint2 = new PointF(mRandom.nextInt(mWidth), mHeight / 2 - mRandom.nextInt(mHeight / 2));
        PointF endPoint = new PointF(mDrawableWidth + mRandom.nextInt(mWidth - mDrawableWidth), 0);

        LoveTypeEvaluator loveTypeEvaluator = new LoveTypeEvaluator(controlPoint1, controlPoint2);

        ValueAnimator valueAnimator = ObjectAnimator.ofObject(loveTypeEvaluator, startPoint, endPoint);
        valueAnimator.setDuration(10000);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                Log.d("tag", "point=" + point.x + "---" + point.y);
                loveIv.setX(point.x);
                loveIv.setY(point.y);
                float animatedFraction = animation.getAnimatedFraction();
                loveIv.setAlpha(1 - animatedFraction);
            }
        });
        return valueAnimator;
    }


}
