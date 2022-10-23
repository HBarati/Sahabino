package com.sahabino.sahabinoBackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class AlertType1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logID;
    String logLevel;
    String logMessage;
}
