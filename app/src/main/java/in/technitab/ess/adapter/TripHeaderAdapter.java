package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.model.Trip;
import in.technitab.ess.model.TripResponse;


public class TripHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TripResponse> mEventArrayList;
    private TripAdapter.TripListener mItemClickListener;
    private int viewUser;
    private SparseIntArray listPosition = new SparseIntArray();


    public TripHeaderAdapter(int viewUser, ArrayList<TripResponse> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
        this.viewUser = viewUser;

    }

    public class TripHeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.status)
        public TextView status;
        @BindView(R.id.tripRecyclerView)
        public RecyclerView tecRecyclerView;
        @BindView(R.id.rootLayout)
        RelativeLayout rootLayout;

        TripHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_trip_header, parent, false);
        return new TripHeaderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final TripHeaderHolder cellViewHolder = (TripHeaderHolder) holder;
        final TripResponse tecResponse = mEventArrayList.get(position);

        ArrayList<Trip> mNewTecArrayList = mEventArrayList.get(position).getTripArrayList();
        if (mNewTecArrayList.size() >0) {
            cellViewHolder.status.setText(tecResponse.getStatus());
            cellViewHolder.tecRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            cellViewHolder.tecRecyclerView.setLayoutManager(layoutManager);
            TripAdapter adapter = new TripAdapter(position,viewUser,mNewTecArrayList);
            cellViewHolder.tecRecyclerView.setAdapter(adapter);
            adapter.setListener(mItemClickListener);

        }else {
            cellViewHolder.rootLayout.setVisibility(View.GONE);
        }
        int lastSeenFirstPosition = listPosition.get(position, 0);
        if (lastSeenFirstPosition >= 0) {
            cellViewHolder.tecRecyclerView.scrollToPosition(lastSeenFirstPosition);
        }

    }

    @Override
    public int getItemCount() {
        return mEventArrayList.size();
    }

    public void SetOnItemClickListener(TripAdapter.TripListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
