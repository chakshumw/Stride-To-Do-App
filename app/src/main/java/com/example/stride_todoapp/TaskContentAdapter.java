package com.example.stride_todoapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskContentAdapter extends RecyclerView.Adapter<TaskContentAdapter.ViewHolder> {

    private List<Task.ContentLine> contentLines;
    private LayoutInflater inflater;
    private RecyclerView recyclerView;

    public TaskContentAdapter(Context context, RecyclerView recyclerView) {
        this.inflater = LayoutInflater.from(context);
        this.contentLines = new ArrayList<>();
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_task_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task.ContentLine contentLine = contentLines.get(position);
        holder.checkBox.setChecked(contentLine.isChecked());
        holder.editText.setText(contentLine.getText());

        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contentLines.get(holder.getAdapterPosition()).setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            contentLines.get(holder.getAdapterPosition()).setChecked(isChecked);
        });

        holder.deleteButton.setOnClickListener(v -> {
            contentLines.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, contentLines.size());
        });

        // Request focus if this is the last item
        if (position == contentLines.size() - 1) {
            holder.editText.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        return contentLines.size();
    }

    public void addNewLine() {
        contentLines.add(new Task.ContentLine("", false));
        notifyItemInserted(contentLines.size() - 1);
        recyclerView.smoothScrollToPosition(contentLines.size() - 1);
    }

    public List<Task.ContentLine> getContentLines() {
        return contentLines;
    }

    public void setContentLines(List<Task.ContentLine> contentLines) {
        this.contentLines = contentLines;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        EditText editText;
        ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            editText = itemView.findViewById(R.id.editText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
