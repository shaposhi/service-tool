package com.example.servicetools.dto;

public class ColumnToObjectMappingRequest {
    private String jsonPath;
    private String mainColumnName;
    private String alternateColumnNames;

    public String getJsonPath() { return jsonPath; }
    public void setJsonPath(String jsonPath) { this.jsonPath = jsonPath; }

    public String getMainColumnName() { return mainColumnName; }
    public void setMainColumnName(String mainColumnName) { this.mainColumnName = mainColumnName; }

    public String getAlternateColumnNames() { return alternateColumnNames; }
    public void setAlternateColumnNames(String alternateColumnNames) { this.alternateColumnNames = alternateColumnNames; }
}


