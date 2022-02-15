package com.example.todoapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    RecyclerView table;
    FloatingActionButton addNewTodo;
    ActivityResultLauncher<Intent> addTaskActivityReturnLauncher;
    ToDoManager manager;
    TodoRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = ((MyApp)getApplication()).appManager;
        addNewTodo = findViewById(R.id.addtask);
        addNewTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewTaskIntent = new Intent(getApplicationContext(),AddToDoActivity.class);
                addTaskActivityReturnLauncher.launch(addNewTaskIntent);
            }
        });

        addTaskActivityReturnLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                          if (result.getResultCode() == Activity.RESULT_OK){
                              Bundle bundle = result.getData().getExtras();
                              String t = bundle.getString("task");
                              String d = bundle.getString("date");
                              ToDo newTodo = new ToDo(t,d);
                              manager.addNewTodo(newTodo);
                              adapter.notifyDataSetChanged();// refresh the table

                          }
                    }
                }
        );

        table = findViewById(R.id.recyclerview_todos);
        adapter = new TodoRecyclerAdapter(manager.listOfTodos,this);
        table.setAdapter(adapter);
        table.setLayoutManager(new LinearLayoutManager(this));

    }
}