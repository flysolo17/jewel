package com.example.trash_scan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    Context context;
    List<String> routeList;

    public RouteAdapter(Context context, List<String> routeList) {
        this.context = context;
        this.routeList = routeList;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RouteViewHolder(LayoutInflater.from(context).inflate(R.layout.row_routes,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        holder.textRoute.setText(routeList.get(position));
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView textRoute;
        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            textRoute = itemView.findViewById(R.id.textRoute);
        }
    }
}