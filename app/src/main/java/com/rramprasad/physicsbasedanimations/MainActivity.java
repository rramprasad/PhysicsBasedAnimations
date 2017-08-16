package com.rramprasad.physicsbasedanimations;

import android.os.Handler;
import android.support.animation.DynamicAnimation;
import android.support.animation.DynamicAnimation.OnAnimationEndListener;
import android.support.animation.FlingAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private boolean appLogoImageViewVisible;
    private int mWidthPixels;
    private int mHeightPixels;
    private ImageView mAppLogoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mWidthPixels = displayMetrics.widthPixels;
        mHeightPixels = displayMetrics.heightPixels;

        mAppLogoImageView = findViewById(R.id.app_logo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAddingElements();
    }

    private void startAddingElements() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSpringAnimation();
            }
        },500);
    }

    private void startSpringAnimation() {
        SpringAnimation springAnimation = new SpringAnimation(mAppLogoImageView, DynamicAnimation.TRANSLATION_Y, mHeightPixels/2);
        SpringForce springForce = springAnimation.getSpring();
        springForce.setStiffness(SpringForce.STIFFNESS_VERY_LOW)
                .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimation.setSpring(springForce);
        VelocityTracker velocityTracker = VelocityTracker.obtain();
        velocityTracker.computeCurrentVelocity(0);
        springAnimation.setStartVelocity(velocityTracker.getYVelocity());
        springAnimation.setStartValue(-100);
        springAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                if(!appLogoImageViewVisible && value > 0){
                    mAppLogoImageView.setVisibility(View.VISIBLE);
                    appLogoImageViewVisible = true;
                }
            }
        });
        springAnimation.addEndListener(new OnAnimationEndListener(){
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                mAppLogoImageView.setX(mWidthPixels/2);
                mAppLogoImageView.setY(0);
                mAppLogoImageView.setVisibility(View.GONE);
                appLogoImageViewVisible = false;
                startSpringAnimation();
                //startFlingAnimation();
            }
        });
        springAnimation.start();
        velocityTracker.recycle();
    }

    /*private void startFlingAnimation() {
        FlingAnimation flingAnimation = new FlingAnimation(mAppLogoImageView, DynamicAnimation.TRANSLATION_Y);

        VelocityTracker velocityTracker = VelocityTracker.obtain();
        velocityTracker.computeCurrentVelocity(0);
        flingAnimation.setStartVelocity(velocityTracker.getYVelocity());

        flingAnimation.setMinValue(-200)
                .setMaxValue(100)
                .setFriction(1.1f);

        flingAnimation.setStartValue(-100);
        flingAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                if(!appLogoImageViewVisible && value > 0){
                    mAppLogoImageView.setVisibility(View.VISIBLE);
                    appLogoImageViewVisible = true;
                }
            }
        });
        flingAnimation.addEndListener(new OnAnimationEndListener(){
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                mAppLogoImageView.setX(mWidthPixels/2);
                mAppLogoImageView.setY(0);
                mAppLogoImageView.setVisibility(View.GONE);
                appLogoImageViewVisible = false;
            }
        });
        flingAnimation.start();
        velocityTracker.recycle();
    }*/
}
