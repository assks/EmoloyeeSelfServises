package in.technitab.ess.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import in.technitab.ess.R;
import in.technitab.ess.listener.OnBottomReachedListener;
import in.technitab.ess.model.OrgAssetType;
import in.technitab.ess.viewholder.FixAssetHolder;
import in.technitab.ess.viewholder.LoadMoreViewHolder;

public class OrgAssetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean isLoading;
    private Activity activity;
    private List<OrgAssetType> mEvents;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnBottomReachedListener onBottomReachedListener;

    public OrgAssetAdapter(Activity activity, List<OrgAssetType> mEvents, RecyclerView recyclerView) {
        this.activity = activity;
        this.mEvents = mEvents;

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

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_fix_asset, parent, false);
            return new FixAssetHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_load_more, parent, false);
            return new LoadMoreViewHolder(view);
        }
        return null;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        if (holder instanceof FixAssetHolder) {
            final FixAssetHolder userViewHolder = (FixAssetHolder) holder;
            OrgAssetType project = mEvents.get(userViewHolder.getAdapterPosition());
            userViewHolder.name.setText(project.getFixAsset());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_photo);
            requestOptions.error(R.drawable.ic_photo);
            requestOptions.circleCrop();

            Glide.with(activity).setDefaultRequestOptions(requestOptions).load(project.getImagePath()).into(userViewHolder.assetImage);
            userViewHolder.description.setText(project.getDescription());
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
