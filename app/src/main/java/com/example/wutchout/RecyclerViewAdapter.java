package com.example.wutchout;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        TextView name, type;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            name = (TextView) itemView.findViewById(R.id.ListFilename);
            // type = (TextView) itemView.findViewById(R.id.ListFiletype);
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
        String name= data.getName().substring(0, data.getName().length() - 4);

        holder.name.setText(name);
        // holder.type.setText(data.getType());
    }
}