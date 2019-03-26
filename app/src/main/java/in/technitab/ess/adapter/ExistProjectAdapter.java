package in.technitab.ess.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.technitab.ess.R;
import in.technitab.ess.listener.OnBottomReachedListener;
import in.technitab.ess.model.Project;
import in.technitab.ess.util.UserPref;
import in.technitab.ess.viewholder.ExistingProjectHolder;
import in.technitab.ess.viewholder.LoadMoreViewHolder;

public class ExistProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean isLoading;
    private Activity activity;
    private List<Project> mEvents;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private String action;

    private OnBottomReachedListener onBottomReachedListener;

    public ExistProjectAdapter(Activity activity, String action, List<Project> mEvents, RecyclerView recyclerView) {
        this.activity = activity;
        this.mEvents = mEvents;
        this.action = action;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onBottomReachedListener != null) {
                        onBottomReachedListener.onLoadMore();
                    }
                    isLoading = true;
                }

            }
        });
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_tec_project_item, parent, false);
            return new ExistingProjectHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_load_more, parent, false);
            return new LoadMoreViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserPref userPref = new UserPref(activity);
        if (holder instanceof ExistingProjectHolder) {

            final ExistingProjectHolder userViewHolder = (ExistingProjectHolder) holder;
            Project project = mEvents.get(userViewHolder.getAdapterPosition());
            userViewHolder.type.setText(project.getProjectName());
            userViewHolder.status.setVisibility(View.GONE);
            if (action.equalsIgnoreCase(activity.getResources().getString(R.string.approve)))
                userViewHolder.description.setText(activity.getResources().getString(R.string.project_type_view_customer, project.getName(), project.getProjectType(), project.getCustomerName()));
            else
                userViewHolder.description.setText(activity.getResources().getString(R.string.project_type_customer, project.getProjectType(), project.getCustomerName()));
            userViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBottomReachedListener.onRootClickListener(userViewHolder,userViewHolder.getAdapterPosition());
                }
            });

        } else if (holder instanceof LoadMoreViewHolder) {
            LoadMoreViewHolder loadingViewHolder = (LoadMoreViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mEvents == null ? 0 : mEvents.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mEvents.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
}
