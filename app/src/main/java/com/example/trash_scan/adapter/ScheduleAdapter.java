package com.example.trash_scan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.trash_scan.R;
import com.example.trash_scan.RouteAdapter;
import com.example.trash_scan.firebase.models.Collector;

import com.example.trash_scan.firebase.models.Schedule;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>{
    Context context;
    List<Schedule> scheduleList;
    public ScheduleAdapter(Context context,List<Schedule> scheduleList) {
        this.context  = context;
        this.scheduleList = scheduleList;
    }
    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_scheds,parent,false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.initRoutes(schedule.getRoutes());
        holder.displayCollectorInfo(schedule.getCollectorID());
        holder.textTime.setText(schedule.getStartTime().getHour() + " : " + schedule.getStartTime().getMinute() + " " + schedule.getStartTime().getMeridiem());
        for (String days:
             schedule.getDays()) {
            addDays(days,holder.layoutDays);

        }
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{
        LinearLayout layoutDays ;
        RecyclerView layoutRoutes;
        TextView textCollectorName;
        TextView textPlateNumber,textTime;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        private RouteAdapter routeAdapter;
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutDays   = itemView.findViewById(R.id.layoutDays);
            layoutRoutes   = itemView.findViewById(R.id.layoutRoutes);
            textCollectorName  = itemView.findViewById(R.id.textCollectorName);
            textPlateNumber   = itemView.findViewById(R.id.textPlateNumber);
            textTime =  itemView.findViewById(R.id.textTime);
        }
        void initRoutes(List<String> listRoutes ) {
            routeAdapter =new RouteAdapter(itemView.getContext(),listRoutes);
            layoutRoutes.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            layoutRoutes.setAdapter(routeAdapter);

        }
        void displayCollectorInfo(String collectorID) {
            firestore.collection(Collector.TABLE_NAME)
                    .document(collectorID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Collector collector = documentSnapshot.toObject(Collector.class);
                            if (collector != null) {
                                textCollectorName.setText(collector.getFirstName() + " " + collector.getLastName());
                                textPlateNumber.setText(collector.getPlateNumber());
                            }
                        }
                    });
        }

    }

    private void addDays(String days,LinearLayout linearLayout) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_days,linearLayout,false);
        TextView textDays = view.findViewById(R.id.texDayName);
        textDays.setText(days);
        linearLayout.addView(view);
    }
}
