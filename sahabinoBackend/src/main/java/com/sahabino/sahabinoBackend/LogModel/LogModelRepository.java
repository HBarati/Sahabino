package com.sahabino.sahabinoBackend.LogModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogModelRepository extends JpaRepository<LogModel,Long> {

}
