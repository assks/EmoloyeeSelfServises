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
import in.technitab.ess.listener.ViewHeaderListener;
import in.technitab.ess.model.NewTec;
import in.technitab.ess.model.TecResponse;


public class TecHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TecResponse> mEventArrayList;
    private ViewHeaderListener mItemClickListener;
    private int viewUser;
    private SparseIntArray listPosition = new SparseIntArray();


    public TecHeaderAdapter(int viewUser,ArrayList<TecResponse> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
        this.viewUser = viewUser;

    }

    public class TecHeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        public TextView status;
        @BindView(R.id.timeSheetRecyclerView)
        public RecyclerView tecRecyclerView;
        @BindView(R.id.rootLayout)
        RelativeLayout rootLayout;

        TecHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tec_header, parent, false);
        return new TecHeaderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final TecHeaderHolder cellViewHolder = (TecHeaderHolder) holder;
        final TecResponse tecResponse = mEventArrayList.get(position);

        ArrayList<NewTec> mNewTecArrayList = mEventArrayList.get(position).getTecArrayList();
        if (mNewTecArrayList.size() >0) {
            cellViewHolder.status.setText(tecResponse.getStatus());
            cellViewHolder.tecRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            cellViewHolder.tecRecyclerView.setLayoutManager(layoutManager);
            NewTecAdapter adapter = new NewTecAdapter(position,viewUser,mNewTecArrayList);
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

    public void SetOnItemClickListener(ViewHeaderListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
