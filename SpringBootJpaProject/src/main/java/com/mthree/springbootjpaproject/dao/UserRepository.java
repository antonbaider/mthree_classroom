package com.mthree.springbootjpaproject.dao;

import com.mthree.springbootjpaproject.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

}
