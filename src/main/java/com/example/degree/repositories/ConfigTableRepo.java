package com.example.degree.repositories;

import com.example.degree.dto.ConfigDTO;
import com.example.degree.entities.ConfigTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigTableRepo extends JpaRepository<ConfigTable,Long> {
    List<ConfigTable> findByMasterType_Id(Long masterTypeId);

    Optional<ConfigTable> findByMasterType_IdAndValue(Long masterId, String value);

    @Query("SELECT c FROM ConfigTable c WHERE c.masterType.id = :masterTypeId AND c.value = :value")
    Optional<ConfigTable> findByMasterTypeIdAndValue(@Param("masterTypeId") Long masterTypeId, @Param("value") String value);




}
