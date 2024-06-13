package com.example.degree.repositories;

import com.example.degree.entities.DocumentTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepo extends JpaRepository<DocumentTable, Long> {
    @Query("SELECT d FROM DocumentTable d WHERE d.degree.id = :degreeId")
    Optional<DocumentTable> findByDegreeId(@Param("degreeId") Long degreeId);
}
