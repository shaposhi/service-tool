package com.example.servicetools.dto;

import lombok.Data;

@Data
public class SimpleClient {
    private long ce;
    private String type;
    private String isClient;  //todo change to boolean
    private String isVetted;  //todo change to boolean
    private String country;



}
