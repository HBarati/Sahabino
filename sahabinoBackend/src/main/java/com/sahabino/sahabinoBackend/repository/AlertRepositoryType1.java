package com.sahabino.sahabinoBackend.repository;

import com.sahabino.sahabinoBackend.entity.AlertType1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepositoryType1 extends JpaRepository<AlertType1,Long> {

}
