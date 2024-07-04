package com.example.degree.repositories;

import com.example.degree.entities.UserGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepo extends JpaRepository<UserGroups, Long> {
    @Query("SELECT ug FROM UserGroups ug WHERE ug.user.id = :userId")
    List<UserGroups> findByUserId(Long userId);

    @Query("SELECT ug FROM UserGroups ug WHERE ug.name = :name AND ug.user.id = :userId")
    UserGroups findByNameAndUserId(@Param("name") String name, @Param("userId") Long userId);

    UserGroups findByName(String name);
}
