package in.technitab.ess.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import in.technitab.ess.R;
import in.technitab.ess.databinding.LayoutGuesthouseBookingItemBinding;
import in.technitab.ess.model.GuesthouseBooking;

public class GuesthouseAdapter extends RecyclerView.Adapter<GuesthouseAdapter.GuesthouseViewHolder> {

    private List<GuesthouseBooking> guesthouseBookingList;

    public GuesthouseAdapter(List<GuesthouseBooking> guesthouseBookingList) {
        this.guesthouseBookingList = guesthouseBookingList;
    }

    @NonNull
    @Override
    public GuesthouseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutGuesthouseBookingItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.layout_guesthouse_booking_item,viewGroup,false);
        return new GuesthouseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GuesthouseViewHolder holder, int position) {
        holder.binding.setBooking(guesthouseBookingList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return guesthouseBookingList.size();
    }

    public class GuesthouseViewHolder extends RecyclerView.ViewHolder {
        LayoutGuesthouseBookingItemBinding binding;
        public GuesthouseViewHolder(LayoutGuesthouseBookingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
