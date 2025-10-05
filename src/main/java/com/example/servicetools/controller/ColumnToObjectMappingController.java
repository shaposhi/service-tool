package com.example.servicetools.controller;

import com.example.servicetools.dto.ColumnToObjectMappingRequest;
import com.example.servicetools.model.ColumnToObjectMapping;
import com.example.servicetools.service.ColumnToObjectMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/column-mappings")
public class ColumnToObjectMappingController {

    @Autowired
    private ColumnToObjectMappingService service;

    // Create
    @PostMapping
    public ResponseEntity<ColumnToObjectMapping> create(@RequestBody ColumnToObjectMappingRequest request) {
        try {
            ColumnToObjectMapping entity = service.create(
                request.getJsonPath(),
                request.getMainColumnName(),
                request.getAlternateColumnNames()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(entity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Read - all
    @GetMapping
    public ResponseEntity<List<ColumnToObjectMapping>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Read - paginated
    @GetMapping("/paginated")
    public ResponseEntity<Page<ColumnToObjectMapping>> findAllPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        return ResponseEntity.ok(service.findAll(page, size, sortBy, sortDirection));
    }

    // Read - by id
    @GetMapping("/{id}")
    public ResponseEntity<ColumnToObjectMapping> findById(@PathVariable Long id) {
        Optional<ColumnToObjectMapping> result = service.findById(id);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Search endpoints
    @GetMapping("/search/json-path")
    public ResponseEntity<Page<ColumnToObjectMapping>> searchByJsonPath(
        @RequestParam String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        return ResponseEntity.ok(service.searchByJsonPath(q, page, size, sortBy, sortDirection));
    }

    @GetMapping("/search/main-column")
    public ResponseEntity<Page<ColumnToObjectMapping>> searchByMainColumn(
        @RequestParam String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        return ResponseEntity.ok(service.searchByMainColumn(q, page, size, sortBy, sortDirection));
    }

    @GetMapping("/search/alternate-columns")
    public ResponseEntity<Page<ColumnToObjectMapping>> searchByAlternateColumns(
        @RequestParam String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        return ResponseEntity.ok(service.searchByAlternateColumns(q, page, size, sortBy, sortDirection));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ColumnToObjectMapping> update(@PathVariable Long id, @RequestBody ColumnToObjectMappingRequest request) {
        try {
            ColumnToObjectMapping updated = new ColumnToObjectMapping();
            updated.setJsonPath(request.getJsonPath());
            updated.setMainColumnName(request.getMainColumnName());
            updated.setAlternateColumnNames(request.getAlternateColumnNames());
            ColumnToObjectMapping saved = service.update(id, updated);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }

}


