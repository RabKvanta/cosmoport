package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShipSpecification {

   // implements Specification<Ship>  private SearchCriteria criteria;
/*
    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return null;
    }

*/
   public static Specification<Ship> forIsUsed(Boolean isUsed){
        return (ship, cq, cb) -> cb.equal(ship.get("isUsed"),isUsed);
    }
    public static Specification<Ship> forShipType(ShipType shipType){
        return (ship, cq, cb) -> cb.equal(ship.get("shipType"),shipType);
    }
    public static Specification<Ship> getAfter(Date after){
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        return (ship, cq, cb) -> cb.greaterThan(ship.get("prodDate"),after);
    }
    public static Specification<Ship> getBefore(Date before){
        return (ship, cq, cb) -> cb.lessThan(ship.get("prodDate"),before);
    }
    public static Specification<Ship> getGreaterRating(Double rating){
        return (ship, cq, cb) -> cb.greaterThanOrEqualTo(ship.get("rating"),rating);
    }
    public static Specification<Ship> getLessRating(Double rating){
        return (ship, cq, cb) -> cb.lessThanOrEqualTo(ship.get("rating"),rating);
    }
    public static Specification<Ship> getGreaterSpeed(Double speed){
        return (ship, cq, cb) -> cb.greaterThanOrEqualTo(ship.get("speed"),speed);
    }
    public static Specification<Ship> getLessSpeed(Double speed){
        return (ship, cq, cb) -> cb.lessThanOrEqualTo(ship.get("speed"),speed);
    }
    public static Specification<Ship> getGreaterCrewSize(Integer crewSize){
        return (ship, cq, cb) -> cb.greaterThanOrEqualTo(ship.get("crewSize"),crewSize);
    }
    public static Specification<Ship> getLessCrewSize(Integer crewSize){
        return (ship, cq, cb) -> cb.lessThanOrEqualTo(ship.get("crewSize"),crewSize);
    }

    public static Specification<Ship> betweenSpeed(Double minSpeed, double maxSpeed){

        return (ship, cq, cb) -> cb.between(ship.get("speed"),minSpeed, maxSpeed);
    }
    public static Specification<Ship> betweenRating(Double minRating, double maxRating){
        return (ship, cq, cb) -> cb.between(ship.get("rating"),minRating, maxRating);
    }
    public static Specification<Ship> betweenCrewSize(Integer minCrewSize, Integer maxCrewSize){
        return (ship, cq, cb) -> cb.between(ship.get("crewSize"),minCrewSize, maxCrewSize);
    }
    public static Specification<Ship> planetContains(String name) {
        return (ship, cq, cb) -> cb.like(ship.get("planet"), "%" + name + "%");
    }

    public static Specification<Ship> nameContains(String name) {
        return (ship, cq, cb) -> cb.like(ship.get("name"), "%" + name + "%");
    }

}
