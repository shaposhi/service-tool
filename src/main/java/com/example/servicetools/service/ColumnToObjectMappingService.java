package com.example.servicetools.service;

import com.example.servicetools.dao.ColumnToObjectMappingRepository;
import com.example.servicetools.model.ColumnToObjectMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ColumnToObjectMappingService {

    @Autowired
    private ColumnToObjectMappingRepository repository;

    // Create
    public ColumnToObjectMapping create(String jsonPath, String mainColumnName, String alternateColumnNames) {
        ColumnToObjectMapping entity = new ColumnToObjectMapping();
        entity.setJsonPath(jsonPath);
        entity.setMainColumnName(mainColumnName);
        entity.setAlternateColumnNames(alternateColumnNames);
        return repository.save(entity);
    }

    public ColumnToObjectMapping save(ColumnToObjectMapping entity) {
        return repository.save(entity);
    }

    // Read
    public List<ColumnToObjectMapping> findAll() {
        return repository.findAll();
    }

    public Page<ColumnToObjectMapping> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ColumnToObjectMapping> findAll(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }

    public Optional<ColumnToObjectMapping> findById(Long id) {
        return repository.findById(id);
    }

    // Search helpers
    public Page<ColumnToObjectMapping> searchByJsonPath(String query, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByJsonPathContainingIgnoreCase(query, pageable);
    }

    public Page<ColumnToObjectMapping> searchByMainColumn(String query, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByMainColumnNameContainingIgnoreCase(query, pageable);
    }

    public Page<ColumnToObjectMapping> searchByAlternateColumns(String query, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByAlternateColumnNamesContainingIgnoreCase(query, pageable);
    }

    // Update
    public ColumnToObjectMapping update(Long id, ColumnToObjectMapping updated) {
        Optional<ColumnToObjectMapping> existing = repository.findById(id);
        if (existing.isPresent()) {
            ColumnToObjectMapping entity = existing.get();
            entity.setJsonPath(updated.getJsonPath());
            entity.setMainColumnName(updated.getMainColumnName());
            entity.setAlternateColumnNames(updated.getAlternateColumnNames());
            return repository.save(entity);
        }
        throw new RuntimeException("ColumnToObjectMapping not found with id: " + id);
    }

    // Delete
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("ColumnToObjectMapping not found with id: " + id);
        }
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public long count() { return repository.count(); }
    public boolean exists(Long id) { return repository.existsById(id); }
}


