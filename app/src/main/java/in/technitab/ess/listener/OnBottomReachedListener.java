package in.technitab.ess.listener;

import android.support.v7.widget.RecyclerView;

public interface OnBottomReachedListener {

    void onLoadMore();
    void onRootClickListener(RecyclerView.ViewHolder viewHolder, int position);
}
