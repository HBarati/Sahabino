package com.sahabino.sahabinoBackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

public class DGIMEntities {
}

@Getter
@Setter
@Entity
@Table
class INFO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logID;
    int oneCount;
}

@Getter
@Setter
@Entity
@Table
class DEBUG {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logID;
    int oneCount;
}

@Getter
@Setter
@Entity
@Table
class ERROR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logID;
    int oneCount;
}

@Getter
@Setter
@Entity
@Table
class WARN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logID;
    int oneCount;
}

@Getter
@Setter
@Entity
@Table
class FATAL {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logID;
    int oneCount;
}
