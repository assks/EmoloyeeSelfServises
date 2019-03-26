package in.technitab.ess.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.model.Task;

public class PendingTaskAdapter extends RecyclerView.Adapter<PendingTaskAdapter.ViewHolder> {

    private ArrayList<Task> mTaskArrayList;
    private TaskListener listener;

    public PendingTaskAdapter(ArrayList<Task> mTaskArrayList) {
        this.mTaskArrayList = mTaskArrayList;
    }

    public void setOnItemClickListener(TaskListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pending_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Task taskItem = mTaskArrayList.get(position);
        holder.task.setText(taskItem.getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelected(holder,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task)
        public CheckBox task;

        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }
    }


    public interface TaskListener {
        void onSelected(RecyclerView.ViewHolder viewHolder, int position);
    }
}
