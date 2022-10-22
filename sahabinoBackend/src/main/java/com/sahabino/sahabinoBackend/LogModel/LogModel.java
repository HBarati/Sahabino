package com.sahabino.sahabinoBackend.LogModel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class LogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long logID;
    String logLevel;
    String logMessage;
}
