package com.sahabino.sahabinoBackend.controller;

import com.sahabino.sahabinoBackend.entity.AlertType1;

import com.sahabino.sahabinoBackend.entity.AlertType2;
import com.sahabino.sahabinoBackend.entity.AlertType3;
import com.sahabino.sahabinoBackend.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "api/")
public class AlertController {

    private final AlertService alertService;

    @Autowired
    public AlertController(AlertService alertType1Service) {
        this.alertService = alertType1Service;
    }

    @GetMapping("alertTyp1")
    public List<AlertType1> getAlertType1() {
        return alertService.getAlertType1();
    }

    @GetMapping("alertTyp2")
    public List<AlertType2> getAlertType2() {
        return alertService.getAlertType2();
    }

    @GetMapping("alertTyp3")
    public List<AlertType3> getAlertType3() {
        return alertService.getAlertType3();
    }
}
