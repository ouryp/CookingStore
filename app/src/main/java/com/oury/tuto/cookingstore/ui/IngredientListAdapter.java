package com.oury.tuto.cookingstore.ui;
import com.oury.tuto.cookingstore.R;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.oury.tuto.cookingstore.data.CookingIngredient;

public class IngredientListAdapter extends ListAdapter<CookingIngredient, IngredientListAdapter.ViewHolder> {

    public static final int PROPRIETY_BOX_SIZE_LOW = 0;
    public static final int PROPRIETY_BOX_SIZE_HIGH = 1;

    private static ListViewEvent event;
    private static int boxSize;

    public IngredientListAdapter(ListViewEvent eventCallback, int proprietyBox) {
        super(new listDiff());
        event = eventCallback;
        boxSize = proprietyBox;
    }

    @NonNull
    @Override
    public IngredientListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CookingIngredient current = getItem(position);
        holder.bind(current, position);
    }

    public static class listDiff extends DiffUtil.ItemCallback<CookingIngredient> {
        @Override
        public boolean areItemsTheSame(@NonNull CookingIngredient oldItem, @NonNull CookingIngredient newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CookingIngredient oldItem, @NonNull CookingIngredient newItem) {
            return oldItem.toRoom().equals(newItem.toRoom());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textIngredient;
        private final TextView textQuantity;

        private ViewHolder(View itemView) {
            super(itemView);
            textIngredient = itemView.findViewById(R.id.ingredient_list_name);
            textQuantity = itemView.findViewById(R.id.ingredient_list_quantity);
            if(boxSize == PROPRIETY_BOX_SIZE_HIGH) {
                textIngredient.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                textQuantity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            }
        }

        public void bind(CookingIngredient ingredient, int position) {
            textIngredient.setText(ingredient.getIngredient());
            textQuantity.setText(ingredient.getQuantity());
        }

        static ViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list, parent, false);
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
