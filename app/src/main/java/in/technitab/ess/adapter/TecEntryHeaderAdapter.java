package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.listener.ViewHeaderListener;
import in.technitab.ess.model.NewTecEntry;
import in.technitab.ess.model.TecEntryResponse;


public class TecEntryHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TecEntryResponse> mEventArrayList;
    private ViewHeaderListener mItemClickListener;
    private SparseIntArray listPosition = new SparseIntArray();

    public TecEntryHeaderAdapter(ArrayList<TecEntryResponse> mEventArrayList) {
        this.mEventArrayList = mEventArrayList;
    }

    public class TecHeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        public TextView status;
        @BindView(R.id.amount)
        public TextView amount;
        @BindView(R.id.emp_amount)
        public TextView empAmount;
        @BindView(R.id.ac_amount)
        public TextView acAmount;
        @BindView(R.id.amountLayout)
        public LinearLayout amountLayout;
        @BindView(R.id.timeSheetRecyclerView)
        public RecyclerView tecRecyclerView;
        @BindView(R.id.rootLayout)
        LinearLayout rootLayout;

        TecHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tec_entry_header, parent, false);
        return new TecHeaderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final TecHeaderHolder cellViewHolder = (TecHeaderHolder) holder;
        final TecEntryResponse tecResponse = mEventArrayList.get(position);
        ArrayList<NewTecEntry> mNewTecArrayList = mEventArrayList.get(position).getTecArrayList();

        if (mNewTecArrayList.size() >0) {
            cellViewHolder.status.setText(tecResponse.getCategory());
            cellViewHolder.amountLayout.setVisibility(tecResponse.getTotalAmount() >=1?View.VISIBLE:View.GONE);

            cellViewHolder.amount.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_sigma),null, null, null);
            cellViewHolder.amount.setText(String.valueOf(tecResponse.getTotalAmount()));
            cellViewHolder.empAmount.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_emp_amount),null , null, null);
            cellViewHolder.empAmount.setText(String.valueOf(tecResponse.getEmpPaid()));
            cellViewHolder.acAmount.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_account_amount), null, null, null);
            cellViewHolder.acAmount.setText(String.valueOf(tecResponse.getAccountPaid()));

            cellViewHolder.tecRecyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            cellViewHolder.tecRecyclerView.setLayoutManager(layoutManager);
            NewTecEntryAdapter adapter = new NewTecEntryAdapter(mContext,position,mNewTecArrayList);
            cellViewHolder.tecRecyclerView.setAdapter(adapter);
            adapter.SetOnItemClickListener(mItemClickListener);

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
