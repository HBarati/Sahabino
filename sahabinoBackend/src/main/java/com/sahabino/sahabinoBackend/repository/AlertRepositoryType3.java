package com.sahabino.sahabinoBackend.repository;

import com.sahabino.sahabinoBackend.entity.AlertType3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepositoryType3 extends JpaRepository<AlertType3,Long> {

}
