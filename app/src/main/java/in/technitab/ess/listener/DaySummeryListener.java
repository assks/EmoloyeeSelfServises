package in.technitab.ess.listener;

import android.support.v7.widget.RecyclerView;

public interface DaySummeryListener  {
    void onDelete(RecyclerView.ViewHolder viewHolder, int position);
    void onEdit(RecyclerView.ViewHolder viewHolder, int position);
    void onAddNewEntry(RecyclerView.ViewHolder viewHolder, int position);
}
