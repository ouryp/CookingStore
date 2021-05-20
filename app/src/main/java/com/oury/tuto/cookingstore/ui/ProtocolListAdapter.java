package com.oury.tuto.cookingstore.ui;
import com.google.android.material.textfield.TextInputLayout;
import com.oury.tuto.cookingstore.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ProtocolListAdapter extends ListAdapter<String, ProtocolListAdapter.ViewHolder> {

    private static ListViewEvent event;

    public ProtocolListAdapter(ListViewEvent eventCallback) {
        super(new listDiff());
        event = eventCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String current = getItem(position);
        holder.bind(current, position);
    }

    public static class listDiff extends DiffUtil.ItemCallback<String> {

        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textProtocol;
        private final TextInputLayout textNbr;

        private ViewHolder(View itemView) {
            super(itemView);
            textProtocol = itemView.findViewById(R.id.protocol_list_textView);
            textNbr = itemView.findViewById(R.id.protocol_step);
        }

        public void bind(String text, int position) {
            textProtocol.setText(text);
            textNbr.getEditText().setText(String.valueOf(position + 1));
        }

        public void update(int position) {
            textNbr.getEditText().setText(String.valueOf(position + 1));
        }

        static ViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.protocol_list, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.onListViewClickCallback(v);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    event.onListViewLongClickCallback(v);
                    return true;
                }
            });
            return new ViewHolder(view);
        }

    }
}
