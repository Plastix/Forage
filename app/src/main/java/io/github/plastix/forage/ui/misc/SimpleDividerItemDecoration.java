package io.github.plastix.forage.ui.misc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import javax.inject.Inject;

import io.github.plastix.forage.ApplicationScope;
import io.github.plastix.forage.R;

/**
 * Simple divider decorator for a RecyclerView.
 * Based on https://gist.github.com/polbins/e37206fbc444207c0e92.
 */
public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;

    @Inject
    public SimpleDividerItemDecoration(@ApplicationScope Context context) {
        divider = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}