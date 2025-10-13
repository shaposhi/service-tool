package com.example.servicetools.dto;

import java.util.List;

public class PublishRequest {
    private List<Long> numbers;

    public List<Long> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Long> numbers) {
        this.numbers = numbers;
    }
}