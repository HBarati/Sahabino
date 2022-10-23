package com.sahabino.sahabinoBackend.repository;

import com.sahabino.sahabinoBackend.entity.AlertType2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepositoryType2 extends JpaRepository<AlertType2,Long> {

}
