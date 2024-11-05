package com.mthree.bankmthree.repository;

import com.mthree.bankmthree.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsBySsn(String ssn);

    boolean existsByPhone(String phone);

    @Query("SELECT u FROM User u JOIN u.family f WHERE f.id = :familyMemberId")
    List<User> findUsersByFamilyMemberId(@Param("familyMemberId") Long familyMemberId);

    Optional<User> findWithAccountsByUsername(@NotBlank String username);

}