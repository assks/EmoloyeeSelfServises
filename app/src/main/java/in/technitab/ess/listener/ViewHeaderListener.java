package in.technitab.ess.listener;

import android.support.v7.widget.RecyclerView;

public interface ViewHeaderListener {
    void onItemSelected(RecyclerView.ViewHolder viewHolder, int headerPosition , int position);
}
