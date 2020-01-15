package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface ShipService {
    Ship getById(Long id);
    Optional<Ship> getAllById(Long id);
    void save(Ship ship);
    void delete(Long id);
    Boolean exists(Long id);
    List<Ship> getAll();
    List<Ship> getAll(Specification spec,PageRequest pageRequest);
    List<Ship> getAll(PageRequest pageRequest);
    Long count();
    Long count(Specification spec);
}
