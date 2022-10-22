package com.sahabino.sahabinoBackend.LogModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "api/LogModelType1")
public class LogController {

    private final LogModelService logModelService;

    @Autowired
    public LogController(LogModelService logModelService) {
        this.logModelService = logModelService;
    }

    @GetMapping
    public List<LogModel> getLogModels(){
        return logModelService.getLogModels();
    }
}
