package com.example.degree.repositories;

import com.example.degree.entities.DocumentTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepo extends JpaRepository<DocumentTable, Long> {
}
