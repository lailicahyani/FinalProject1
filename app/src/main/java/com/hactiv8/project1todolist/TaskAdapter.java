package com.hactiv8.project1todolist;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private TaskItemEventListener eventListener;

    public TaskAdapter(List<Task> tasks ,TaskItemEventListener eventListener) {
        this.tasks = tasks;
        this.eventListener=eventListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{

        CheckBox chbTask;
        ImageView imgRemove;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            chbTask=itemView.findViewById(R.id.chb_taskItem_task);
            imgRemove=itemView.findViewById(R.id.img_taskItem_remove);
        }

        public void bindItem(int i){
            chbTask.setText(tasks.get(i).getTitle());

            //Solution =>
            chbTask.setOnCheckedChangeListener(null);
            chbTask.setChecked(tasks.get(i).isDone());

            if (chbTask.isChecked())
            {
                chbTask.setPaintFlags(chbTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else
            {
                chbTask.setPaintFlags(chbTask.getPaintFlags() & (Paint.STRIKE_THRU_TEXT_FLAG));
            }

            chbTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    eventListener.onCheckBoxClicked(tasks.get(i));
                    if (b)
                    {
                        chbTask.setPaintFlags(chbTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else {
                        chbTask.setPaintFlags(chbTask.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                }
            });
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventListener.onDeleteButtonClicked(tasks.get(i));
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    eventListener.onItemLongPressed(tasks.get(i));
                    return false;
                }
            });
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<Task> tasks)
    {
        this.tasks=tasks;
        notifyDataSetChanged();
    }

    public void addTask(Task task){
        tasks.add(task);
        notifyItemInserted(tasks.size()-1);
    }

    public void deleteTask(Task task)
    {
        int pos=-1;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId()==task.getId())
            {
                pos=i;
                break;
            }
        }

        if (pos>-1)
        {
            tasks.remove(pos);
        }

        notifyItemRemoved(pos);
    }

    public interface TaskItemEventListener{
        void onItemLongPressed(Task task);
        void onCheckBoxClicked(Task task);
        void onDeleteButtonClicked(Task task);
    }

}
