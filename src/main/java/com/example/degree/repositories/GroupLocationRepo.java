package com.example.degree.repositories;
import com.example.degree.entities.GroupLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupLocationRepo extends JpaRepository<GroupLocation,Long> {


}
