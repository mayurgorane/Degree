package com.example.degree.repositories;

import com.example.degree.entities.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepo extends JpaRepository<Degree,Long> {
}
