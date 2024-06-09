package com.example.degree.repositories;

import com.example.degree.entities.MasterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterTypeRepo extends JpaRepository<MasterType,Long> {
}
