package com.example.degree.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.degree.entities.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DegreeRepo extends JpaRepository<Degree,Long> {
    @Query("SELECT d FROM Degree d WHERE d.user.id = :userId")
    List<Degree> findByUserId(@Param("userId") Long userId);

    @Query("SELECT d FROM Degree d WHERE d.user.id = :userId")
    Page<Degree> findByUserId(@Param("userId") Long userId, Pageable pageable);


}
