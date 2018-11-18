package com.JCG.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    List<Users> findByName(String name);
    Optional<Users> findById(Integer id);

}
