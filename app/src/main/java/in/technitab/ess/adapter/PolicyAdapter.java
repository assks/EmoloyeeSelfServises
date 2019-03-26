package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.UserDoc;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.ViewHolder> {
    private final int POLICY = 1;
    private final int DOCS = 2;
    private List<Object> mObjectArrayList;
    private Context mContext;
    private RecyclerViewItemClickListener listener;

    public PolicyAdapter(List<Object> mObjectArrayList) {
        this.mObjectArrayList = mObjectArrayList;
    }

    public void setListener(RecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_vendor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == POLICY){
            String name = String.valueOf(mObjectArrayList.get(position));
            holder.vendorName.setText(name);

        }else if (getItemViewType(position) == DOCS){
            UserDoc doc = (UserDoc) mObjectArrayList.get(position);
            holder.vendorName.setText(doc.getDocumentName());
            holder.actionButton.setBackgroundResource(doc.getScanned()== 1?R.drawable.ic_check_circle_file:R.drawable.ic_close_circular_button_symbol);
        }
    }

    @Override
    public int getItemCount() {
        return mObjectArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mObjectArrayList.get(position) instanceof String){
            return POLICY;
        }else{
            return DOCS;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.vendor_name)
        public TextView vendorName;
        @BindView(R.id.action)
        public ImageButton actionButton;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        @OnClick({R.id.action,R.id.rootLayout})
        public void onClick(){
            listener.onClickListener(this,getAdapterPosition());
        }
    }
}
