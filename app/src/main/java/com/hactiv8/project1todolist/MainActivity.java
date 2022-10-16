package com.hactiv8.project1todolist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AddTaskDialog.SaveTaskCallBack, EditTaskDialog.SaveEditCallBack {

    private TaskDao taskDao;
    private TaskAdapter adapter;
    private RecyclerView rvTaskList;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDao=AppDataBase.getAppDataBase(this).getTaskDao();

        List<Task> allTasks= taskDao.getAll();

        rvTaskList=findViewById(R.id.rv_main_list);
        rvTaskList.setLayoutManager(new LinearLayoutManager(this , RecyclerView.VERTICAL , false));
        adapter=new TaskAdapter(allTasks, new TaskAdapter.TaskItemEventListener() {
            @Override
            public void onItemLongPressed(Task task) {
                EditTaskDialog editTaskDialog=EditTaskDialog.newInstance(task);
                editTaskDialog.show(getSupportFragmentManager() , null);
            }

            @Override
            public void onCheckBoxClicked(Task task) {
                task.setDone(!task.isDone());
                int result=taskDao.update(task);
            }

            @Override
            public void onDeleteButtonClicked(Task task) {
                int result=taskDao.delete(task);

                if (result>0)
                {
                    adapter.deleteTask(task);
                }
            }
        });
        rvTaskList.setAdapter(adapter);

        FloatingActionButton fabAdd=findViewById(R.id.fab_main_addTask);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskDialog addTaskDialog =new com.hactiv8.project1todolist.AddTaskDialog();
                addTaskDialog.show(getSupportFragmentManager() , null);
            }
        });

        //search
        EditText etSearch=findViewById(R.id.et_main_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (etSearch.length()>0)
                {
                    List<Task> tasks=taskDao.search(etSearch.getText().toString().trim());

                    adapter.setTasks(tasks);
                }
                else
                {
                    adapter.setTasks(taskDao.getAll());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onSaveCallBack(Task task) {
        long id=taskDao.add(task);

        if (id!=-1)
        {
            task.setId(id);
            adapter.addTask(task);
        }
        else {
            Log.e(TAG, "add Error" );
        }
    }
    @Override
    public void onSaveEditCallBack(Task task) {
        int result=taskDao.update(task);

        if (result>0)
        {
            adapter.editTask(task);
        }
        else {
            Log.e(TAG, "Edit Error" );
        }
    }
}