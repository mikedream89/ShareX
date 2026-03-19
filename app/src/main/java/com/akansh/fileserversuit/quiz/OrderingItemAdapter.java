package com.akansh.fileserversuit.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akansh.fileserversuit.R;
import com.akansh.fileserversuit.quiz.model.OrderingQuestion;

import java.util.List;

public class OrderingItemAdapter extends RecyclerView.Adapter<OrderingItemAdapter.ViewHolder> {

    private final OrderingQuestion question;

    public OrderingItemAdapter(OrderingQuestion question) {
        this.question = question;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ordering_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> items = question.getShuffledItems();
        holder.tvItem.setText(items.get(position));
        holder.tvIndex.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return question.getShuffledItems().size();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        question.moveItem(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(Math.min(fromPosition, toPosition),
                Math.abs(fromPosition - toPosition) + 1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvItem;
        final TextView tvIndex;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_order_item);
            tvIndex = itemView.findViewById(R.id.tv_order_index);
        }
    }
}
