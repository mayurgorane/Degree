package com.example.degree.repositories;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.example.degree.entities.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotesRepo extends JpaRepository<Notes, Long> {

    @Query("SELECT MAX(n.groupId) FROM Notes n WHERE n.degree.degreeId = :degreeId")
    Optional<Long> findMaxGroupIdByDegree(Long degreeId);

    @Query("SELECT n FROM Notes n WHERE n.groupId = :groupId AND n.degree.degreeId = :degreeId ORDER BY n.version DESC")
    List<Notes> findTopByGroupIdAndDegreeDegreeIdOrderByVersionDesc(Long groupId, Long degreeId, Pageable pageable);

    void deleteByGroupIdAndDegreeDegreeId(Long groupId, Long degreeId);

    void deleteByGroupIdAndVersionAndDegreeDegreeId(Long groupId, Long version, Long degreeId);

    @Query("SELECT n FROM Notes n WHERE n.degree.id = :degreeId AND n.groupId = :groupId")
    List<Notes> findByGroupIdAndDegreeId(@Param("groupId") Long groupId, @Param("degreeId") Long degreeId);
}
