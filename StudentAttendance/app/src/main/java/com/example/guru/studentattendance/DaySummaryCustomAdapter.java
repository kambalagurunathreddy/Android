package com.example.guru.studentattendance;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guru on 4/11/2018.
 */

public class DaySummaryCustomAdapter extends RecyclerView.Adapter<DaySummaryCustomAdapter.ViewHolder>{

    private List<Daysummary> listItems;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_view_attendance_adapter_template,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Daysummary listitem = listItems.get(position);
        holder.tv_item_day.setText(listitem.getCheckin_date());
        holder.tv_item_leaves.setText(listitem.getLeaves());
        ArrayList<String> sess_rows = listitem.getSession_rows();
        if (sess_rows.size()==1) {
            holder.tv_item_session1.setText(sess_rows.get(0));
        }
        else if (sess_rows.size()==2){
            holder.tv_item_session1.setText(sess_rows.get(0));
            holder.tv_item_session2.setText(sess_rows.get(1));
        }
        else if (sess_rows.size()==3){
            holder.tv_item_session1.setText(sess_rows.get(0));
            holder.tv_item_session2.setText(sess_rows.get(1));
            holder.tv_item_session3.setText(sess_rows.get(2));
        }
//        holder.tv_item_session2.setText(sess_rows.get(1));
//        holder.tv_item_session3.setText(sess_rows.get(2));



    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public DaySummaryCustomAdapter(List<Daysummary> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView tv_item_day;
        public TextView tv_item_leaves;
        public TextView tv_item_session1;
        public TextView tv_item_session2;
        public TextView tv_item_session3;

        public ViewHolder(View itemView){
            super(itemView);

            tv_item_day = (TextView) itemView.findViewById(R.id.item_day);
            tv_item_leaves = (TextView) itemView.findViewById(R.id.item_leaves);
            tv_item_session1 = (TextView) itemView.findViewById(R.id.item_session1);
            tv_item_session2 = (TextView) itemView.findViewById(R.id.item_session2);
            tv_item_session3 = (TextView) itemView.findViewById(R.id.item_session3);

        }
    }
}
