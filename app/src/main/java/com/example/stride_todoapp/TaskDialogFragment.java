package com.example.stride_todoapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskDialogFragment extends DialogFragment {
    private EditText titleEditText;
    private Button saveButton;
    private Button addButton;
    private Task task;
    private OnTaskSavedListener listener;
    private TaskContentAdapter taskContentAdapter;

    public interface OnTaskSavedListener {
        void onTaskSaved(Task task, int position);
    }

    public TaskDialogFragment(Task task, OnTaskSavedListener listener) {
        this.task = task;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_task, container, false);
        titleEditText = view.findViewById(R.id.titleEditText);
        saveButton = view.findViewById(R.id.saveButton);
        addButton = view.findViewById(R.id.addButton);

        RecyclerView contentRecyclerView = view.findViewById(R.id.contentRecyclerView);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskContentAdapter = new TaskContentAdapter(getContext(), contentRecyclerView);
        contentRecyclerView.setAdapter(taskContentAdapter);

        if (task != null) {
            titleEditText.setText(task.getTitle());
            taskContentAdapter.setContentLines(task.getContentLines());
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskContentAdapter.addNewLine();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                List<Task.ContentLine> contentLines = taskContentAdapter.getContentLines();

                if (task == null) {
                    task = new Task(title, contentLines);
                } else {
                    task.setTitle(title);
                    task.setContentLines(contentLines);
                }

                listener.onTaskSaved(task, getArguments().getInt("position", -1));
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
