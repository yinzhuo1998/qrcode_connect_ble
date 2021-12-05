package com.manong.putSystem.basic;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manong.putSystem.R;


/**
 * 方便灵活进行TitleBar中相关控件的定制
 */
public class TitleBar {
    private View mRoot;
    private TextView mTitleView;
    private ImageView mLeftImageView;
    private TextView mLeftTextView;
    private LinearLayout mLeftLayout;
    private TextView mRightTextView;
    private ImageView mRightImageView;
    private LinearLayout mRightLayout;

    public TitleBar(View root) {
        mRoot = root;
        mTitleView = mRoot.findViewById(R.id.title_text);
        mLeftImageView = mRoot.findViewById(R.id.left_image);
        mLeftTextView = mRoot.findViewById(R.id.left_text);
        mLeftLayout = mRoot.findViewById(R.id.left_layout);
        mRightImageView = mRoot.findViewById(R.id.right_image);
        mRightTextView = mRoot.findViewById(R.id.right_text);
        mRightLayout = mRoot.findViewById(R.id.right_layout);

        mTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        reset();
    }

    public void reset() {
//        mLeftImageView.setVisibility(View.GONE);
        mLeftTextView.setVisibility(View.GONE);
        mRightImageView.setVisibility(View.GONE);
        mRightTextView.setVisibility(View.GONE);

        mRightLayout.setOnClickListener(null);
        mLeftLayout.setOnClickListener(null);
    }

    /**
     * set center title
     *
     * @param text
     */
    public void setTitleText(String text) {
        mTitleView.setText(text);
    }

    /**
     * set the show and gone of the right image
     *
     * @param show
     */
    public void showRightImageView(boolean show) {
        mRightImageView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * set  show or gone of the right text
     *
     * @param show
     */
    public void showRightTextView(boolean show) {
        mRightTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * set the size of right text
     *
     * @param size
     */
    public void setRightTextSize(float size) {
        mRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * set the res id of the right image
     *
     * @param res
     */
    public void setRightImageResource(int res) {
        mRightImageView.setImageResource(res);
    }

    /**
     * set the right text
     *
     * @param text
     */
    public void setRightText(String text) {
        mRightTextView.setText(text);
    }

    /**
     * set the right click event
     *
     * @param l
     */
    public void setRightClickListener(View.OnClickListener l) {
        mRightLayout.setOnClickListener(l);
    }

    /**
     * set res id of left image
     *
     * @param res
     */
    public void setLeftImageResource(int res) {
        mLeftImageView.setImageResource(res);
    }

    /**
     * set show or gone og left text
     *
     * @param show
     */
    public void showLeftTextView(boolean show) {
        mLeftTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setLeftCompoundDrawablesRelativeWithIntrinsicBounds(int left, int top, int right, int bottom) {
        mLeftTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom);
    }

    public void setLeftCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        mLeftTextView.setCompoundDrawables(left, top, right, bottom);
    }

    public void setLeftCompoundDrawablePadding(int padding) {
        mLeftTextView.setCompoundDrawablePadding(padding);
    }

    public void setRightCompoundDrawablesRelativeWithIntrinsicBounds(int left, int top, int right, int bottom) {
        mRightTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom);
    }

    public void setRightCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        mRightTextView.setCompoundDrawables(left, top, right, bottom);
    }

    public void setRightCompoundDrawablePadding(int padding) {
        mRightTextView.setCompoundDrawablePadding(padding);
    }

    public void setLeftText(String text) {
        mLeftTextView.setText(text);
    }

    public void showLeftImageView(boolean show) {
        mLeftImageView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setLeftClickListener(View.OnClickListener l) {
        mLeftLayout.setOnClickListener(l);
    }

    public void setTitleCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        mTitleView.setCompoundDrawables(left, top, right, bottom);
    }

    public void setTitleCompoundDrawablePadding(int padding) {
        mTitleView.setCompoundDrawablePadding(padding);
    }

    public void setTitleClickListener(View.OnClickListener l) {
        mTitleView.setOnClickListener(l);
    }

}
