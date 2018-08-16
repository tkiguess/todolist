package com.example.todolist;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    ArrayList<Todo> findByUsername(String username);

}
