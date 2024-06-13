package com.example.stride_todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener, TaskDialogFragment.OnTaskSavedListener {

    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private SharedPreferences sharedPreferences;

    DrawerLayout drawer_layout;
    ImageButton drawer_toggle;
    NavigationView navigationview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer_layout = findViewById(R.id.drawer_layout);
        drawer_toggle = findViewById(R.id.drawer_toggle);
        navigationview = findViewById(R.id.navigationview);

        drawer_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.open();
            }
        });

        // Get header view and username TextView
        View headerView = navigationview.getHeaderView(0);
        ImageView userImage = headerView.findViewById(R.id.ivUserPP);
        TextView textUsername = headerView.findViewById(R.id.tvDHUsername);

        // Retrieve and display the username from SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Username");
        textUsername.setText(username);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfileScreen.class);
                startActivity(intent);
            }
        });

        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.settings) {
                    Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.developer) {
                    Intent intent = new Intent(MainActivity.this, DeveloperInfoScreen.class);
                    startActivity(intent);
                }

                drawer_layout.close();
                return true;
            }
        });

        taskList = loadTasks();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTaskDialog(null, -1);
            }
        });
    }

    @Override
    public void onTaskClick(int position) {
        showTaskDialog(taskList.get(position), position);
    }

    @Override
    public void onTaskLongClick(int position) {
        showDeleteTaskDialog(position);
    }

    private void showTaskDialog(Task task, int position) {
        TaskDialogFragment dialogFragment = new TaskDialogFragment(task, this);
        Bundle args = new Bundle();
        args.putInt("position", position);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "TaskDialogFragment");
    }

    private void showDeleteTaskDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        builder.setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        taskList.remove(position);
                        taskAdapter.notifyItemRemoved(position);
                        saveTasks();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onTaskSaved(Task task, int position) {
        if (position == -1) {
            taskList.add(task);
            taskAdapter.notifyItemInserted(taskList.size() - 1);
        } else {
            taskList.set(position, task);
            taskAdapter.notifyItemChanged(position);
        }
        saveTasks();
    }

    private List<Task> loadTasks() {
        String tasksJson = sharedPreferences.getString("tasks_list", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        return gson.fromJson(tasksJson, type);
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String tasksJson = gson.toJson(taskList);
        editor.putString("tasks_list", tasksJson);
        editor.apply();
    }
}
