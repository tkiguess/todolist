package com.example.todolist;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

    User findByEmail(String email);
    Long countByEmail(String email);
    Long countByUsername(String username);
    User findByUsername(String username);

}