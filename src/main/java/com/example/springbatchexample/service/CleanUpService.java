package com.example.springbatchexample.service;

import org.springframework.stereotype.Service;

@Service
public class CleanUpService {

    public void cleanUp() {
        System.out.println("Метод, который вызвал тасклет - Step, который не требует обработки");
    }
}
