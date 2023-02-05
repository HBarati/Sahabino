package com.sahabino.sahabinoBackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class AlertType2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logID;
    String componentName;
    Long rate;
    String logLevel;
    @Column(length=1500)
    String description;
}
