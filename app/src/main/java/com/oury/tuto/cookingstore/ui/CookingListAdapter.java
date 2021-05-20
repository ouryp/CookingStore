package com.oury.tuto.cookingstore.ui;

import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.oury.tuto.cookingstore.R;
import com.oury.tuto.cookingstore.data.Cooking;
import com.oury.tuto.cookingstore.data.CookingTag;
import com.oury.tuto.cookingstore.room.CookingConverter;

import java.io.IOException;

public class CookingListAdapter extends ListAdapter<Cooking, CookingListAdapter.ViewHolder> {

    private static ListViewEvent event;

    public CookingListAdapter(@NonNull DiffUtil.ItemCallback<Cooking> diffCallback, ListViewEvent eventCallback) {
        super(diffCallback);
        event = eventCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class listDiff extends DiffUtil.ItemCallback<Cooking> {
        @Override
        public boolean areItemsTheSame(@NonNull Cooking oldItem, @NonNull Cooking newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cooking oldItem, @NonNull Cooking newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;
        private final TextView name;
        private final TextView time;
        private final TextView type;
        private final TextView tags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cooking_list_image);
            name = itemView.findViewById(R.id.cooking_list_name);
            time = itemView.findViewById(R.id.cooking_list_time);
            type = itemView.findViewById(R.id.cooking_list_type);
            tags = itemView.findViewById(R.id.cooking_list_tags);
        }

        public void bind(Cooking cooking) {
            try {
                image.setImageBitmap(cooking.getImage().createThumbnail(new Size(image.getMaxWidth(), image.getMaxHeight())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            name.setText(cooking.getName());
            time.setText(cooking.getTime().print());
            type.setText(cooking.getType().text);
            tags.setText(CookingTag.printTags(cooking.getTags()));
        }

        static ViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cooking_list, parent, false);
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
