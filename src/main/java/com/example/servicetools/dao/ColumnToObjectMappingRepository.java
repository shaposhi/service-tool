package com.example.servicetools.dao;

import com.example.servicetools.model.ColumnToObjectMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnToObjectMappingRepository extends JpaRepository<ColumnToObjectMapping, Long> {

    List<ColumnToObjectMapping> findByJsonPathContainingIgnoreCase(String jsonPath);
    Page<ColumnToObjectMapping> findByJsonPathContainingIgnoreCase(String jsonPath, Pageable pageable);

    List<ColumnToObjectMapping> findByMainColumnNameContainingIgnoreCase(String mainColumnName);
    Page<ColumnToObjectMapping> findByMainColumnNameContainingIgnoreCase(String mainColumnName, Pageable pageable);

    List<ColumnToObjectMapping> findByAlternateColumnNamesContainingIgnoreCase(String alternateColumnNames);
    Page<ColumnToObjectMapping> findByAlternateColumnNamesContainingIgnoreCase(String alternateColumnNames, Pageable pageable);
}


