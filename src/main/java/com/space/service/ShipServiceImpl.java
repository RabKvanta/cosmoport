package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipServiceImpl implements ShipService {
    // @Resource(name="shipRepository")
    @Autowired
    ShipRepository shipRepository;

    @Override
    public Ship getById(Long id) {
        return shipRepository.getOne(id);
    }
    @Override
    public Optional<Ship> getAllById(Long id) {
        return shipRepository.findById(id);    }
    @Override
    public void save(Ship ship) {
        shipRepository.save(ship);
    }

    @Override
    public void delete(Long id) {
        shipRepository.deleteById(id);
    }

    @Override
    public List<Ship> getAll() {
        return shipRepository.findAll(PageRequest.of(0,3, Sort.by("name"))).getContent();
    }

    @Override
    public List<Ship> getAll(PageRequest pageRequest) {
        return shipRepository.findAll(pageRequest).getContent();
    }

    @Override
    public List<Ship> getAll(Specification spec,PageRequest pageRequest) {

        return shipRepository.findAll(spec, pageRequest).getContent();
    }

    @Override
    public Long count() {
        return shipRepository.count();
    }

    @Override
    public Long count(Specification spec) {
        return shipRepository.count(spec);
    }

    @Override
    public Boolean exists(Long id) {
        return shipRepository.existsById(id);
    }
}
