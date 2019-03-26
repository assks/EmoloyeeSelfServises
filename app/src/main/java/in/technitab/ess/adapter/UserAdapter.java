package in.technitab.ess.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<User> mUserArrayList;
    private int prev_position = -1;

    public UserAdapter(ArrayList<User> mUserArrayList) {
        this.mUserArrayList = mUserArrayList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_assign_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        User user = mUserArrayList.get(position);
        holder.selected.setBackgroundResource(user.isSelected()?R.drawable.ic_check_circle:R.drawable.ic_radio_button_unchecked);
        holder.name.setText(user.getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prev_position >=0){
                    mUserArrayList.get(prev_position).setSelected(false);
                }
                prev_position = holder.getAdapterPosition();
                mUserArrayList.get(holder.getAdapterPosition()).setSelected(true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.selected)
        public ImageView selected;

        @BindView(R.id.name)
        public TextView name;

        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this,itemView);
        }
    }
}
