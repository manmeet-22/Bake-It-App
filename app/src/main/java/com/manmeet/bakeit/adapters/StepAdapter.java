package com.manmeet.bakeit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manmeet.bakeit.R;
import com.manmeet.bakeit.pojos.Step;
import com.manmeet.bakeit.utils.OnStepClickListener;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private Context context;
    private List<Step> stepList;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private OnStepClickListener mClickCallback;

    public StepAdapter(Context context, List<Step> stepList) {
        this.context = context;
        this.stepList = stepList;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item,parent,false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        final Step step = stepList.get(position);
        shortDescription = step.getShortDescription();
        holder.stepName.setText(shortDescription);
        holder.stepName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickCallback.onStepSelected(step);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public void setOnClick(OnStepClickListener clickCallBack) {
        this.mClickCallback = clickCallBack;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder{
        TextView stepName;
        public StepViewHolder(View itemView) {
            super(itemView);
            stepName = itemView.findViewById(R.id.step_name);
        }

    }
}
