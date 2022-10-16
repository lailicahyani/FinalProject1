package com.hactiv8.project1todolist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//DAO = database access object
@Dao
public interface TaskDao {

    @Insert
    long add(Task task);

    @Delete
    int delete(Task task);

    @Query("SELECT * FROM tbl_tasks")
    List<Task> getAll();

    @Query("SELECT * FROM tbl_tasks WHERE title LIKE '%' || :query || '%'")
    List<Task> search(String query);
}
