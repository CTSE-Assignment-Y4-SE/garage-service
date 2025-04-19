package com.garage.garage_service.core.repository.dao;

import com.garage.garage_service.core.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarageDao extends JpaRepository<Garage, Long> {

}
