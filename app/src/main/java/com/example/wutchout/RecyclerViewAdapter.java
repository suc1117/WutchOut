package com.example.wutchout;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private List<myFile> file;

    public RecyclerViewAdapter(Activity activity, List<myFile> file) {
        this.activity = activity;
        this.file = file;
    }

    @Override
    public int getItemCount() {
        return file.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView name;
        TextView type;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            name = (TextView) itemView.findViewById(R.id.ListFilename);
            type = (TextView) itemView.findViewById(R.id.ListFiletype);

            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "click " +
                            file.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(activity, "remove " +
                            file.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    removeItemView(getAdapterPosition());
                    return false;
                }
            });
            */
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        myFile data = file.get(position);

        // Data Combine
        holder.name.setText(data.getName());
        holder.type.setText(data.getType());
    }

    private void removeItemView(int position) {
        file.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, file.size()); // 지워진 만큼 다시 채워넣기.
    }
}