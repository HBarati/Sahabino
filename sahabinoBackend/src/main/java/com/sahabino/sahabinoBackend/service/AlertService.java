package com.sahabino.sahabinoBackend.service;

import com.sahabino.sahabinoBackend.entity.AlertType2;
import com.sahabino.sahabinoBackend.entity.AlertType3;
import com.sahabino.sahabinoBackend.repository.AlertRepositoryType1;
import com.sahabino.sahabinoBackend.entity.AlertType1;
import com.sahabino.sahabinoBackend.repository.AlertRepositoryType2;
import com.sahabino.sahabinoBackend.repository.AlertRepositoryType3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {
    private final AlertRepositoryType1 alertRepositoryType1;
    private final AlertRepositoryType2 alertRepositoryType2;
    private final AlertRepositoryType3 alertRepositoryType3;

    @Autowired
    public AlertService(AlertRepositoryType1 alertRepositoryType1, AlertRepositoryType2 alertRepositoryType2, AlertRepositoryType3 alertRepositoryType3) {
        this.alertRepositoryType1 = alertRepositoryType1;
        this.alertRepositoryType2 = alertRepositoryType2;
        this.alertRepositoryType3 = alertRepositoryType3;
    }

    public List<AlertType1> getAlertType1() {
        return alertRepositoryType1.findAll();
    }

    public List<AlertType2> getAlertType2() {
        return alertRepositoryType2.findAll();
    }

    public List<AlertType3> getAlertType3() {
        return alertRepositoryType3.findAll();
    }
}