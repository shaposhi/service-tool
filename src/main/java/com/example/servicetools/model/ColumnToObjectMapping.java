package com.example.servicetools.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_COLUMN_TO_OBJECT_MAPPING")
public class ColumnToObjectMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "JSON_PATH")
    private String jsonPath;
    
    @Column(name = "MAIN_COLUMN_NAME")
    private String mainColumnName;

    @Column(name = "ALTERNATE_COLUMN_NAMES")
    private String alternateColumnNames;

}
