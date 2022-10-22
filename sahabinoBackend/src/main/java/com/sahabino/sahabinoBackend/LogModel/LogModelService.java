package com.sahabino.sahabinoBackend.LogModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogModelService {
    private final LogModelRepository logModelRepository;

    @Autowired
    public LogModelService(LogModelRepository logModelRepository) {
        this.logModelRepository = logModelRepository;
    }

    public List<LogModel> getLogModels(){
        return logModelRepository.findAll();
    }
//    public List<LogModel> getLogModels(){
//        return List.of(
//                new LogModel(
//                        1L,
//                        "ERROR",
//                        "api massage test"
//                )
//        );
//    }
}