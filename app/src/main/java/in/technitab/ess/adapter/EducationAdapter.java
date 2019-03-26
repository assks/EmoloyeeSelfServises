package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.Education;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {

    private ArrayList<Education> mProjectTaskArrayList;
    private RecyclerViewItemClickListener listener;
    private Context context;
    private String prevCollegeType = "";


    public EducationAdapter(Context context, ArrayList<Education> mProjectTaskArrayList) {
        this.mProjectTaskArrayList = mProjectTaskArrayList;
        this.context = context;
    }

    public void setTaskListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_education, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Education education = mProjectTaskArrayList.get(position);
        String collegeType = education.getCollegeType();

        holder.educationType.setText(collegeType);
        holder.college.setText(education.getCollege());
        holder.description.setText(context.getResources().getString(R.string.education_description,education.getYear(),education.getPercentage(),education.getBoard()));
    }


    @Override
    public int getItemCount() {
        return mProjectTaskArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.education_type)
        public TextView educationType;
        @BindView(R.id.college)
        public TextView college;
        @BindView(R.id.description)
        public TextView description;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
