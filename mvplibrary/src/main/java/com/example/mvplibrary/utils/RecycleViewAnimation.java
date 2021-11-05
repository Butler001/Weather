package com.example.mvplibrary.utils;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mvplibrary.R;

/**
 * recyclerView 的动画
 */
public class RecycleViewAnimation {
    /**
     * 底部显示recyclerView 的动画
     * @param recyclerView
     */
    public static void runLayoutAnimation(final RecyclerView recyclerView){
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    /**
     * 右侧动画
     * @param recyclerView
     */
    public static void runLayoutAnimationRight(final RecyclerView recyclerView){
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
