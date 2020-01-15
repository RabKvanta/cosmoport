package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

//import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.space.controller.ShipSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@RestController
@RequestMapping("/rest/ships")
public class ShipRestController {
    @Autowired
    private ShipService shipService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> getShip(@PathVariable("id") Long shipId){

        if (shipId == null || shipId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!this.shipService.exists(shipId))
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);;

            Optional<Ship> shipOpt = this.shipService.getAllById(shipId);
            Ship ship = shipOpt.get();
            return new ResponseEntity<>(ship,HttpStatus.OK);

    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> saveShip( @RequestBody @Validated Ship ship) throws ParseException {
        HttpHeaders headers = new HttpHeaders();

        if (ship == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String name = ship.getName();
        String planet = ship.getPlanet();
        ShipType shipType = ship.getShipType();
        Date prodDate = ship.getProdDate();
        Double speed = ship.getSpeed();
        Integer crewSize = ship.getCrewSize();
        Boolean isUsed = ship.getUsed();
        if ( name == null || planet == null || shipType == null
                || prodDate == null || speed == null || crewSize == null ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         }
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        Date dateBefore  = df.parse("2800");
        Date dateAfter = df.parse("3019");
        if (name.isEmpty() || planet.isEmpty() || (name.length() > 50) || (planet.length() > 50)
                || (crewSize < 1) || (crewSize > 9999L) || (speed < 0.01) || (speed > 0.99)
                || (prodDate.getTime() < 0)
                ||    prodDate.before(dateBefore) || prodDate.after(dateAfter)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (isUsed == null)
                        ship.setUsed(false);


        Double R = ship.computR();
        ship.setRating(R);
        this.shipService.save(ship);

        return new ResponseEntity<>(ship,headers,HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> updateShip(@PathVariable("id") Long shipId, @RequestBody @Validated Ship ship, UriComponentsBuilder builder) throws ParseException {
        HttpHeaders headers = new HttpHeaders();

        if (!(shipId instanceof Long) || shipId == null || shipId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ship == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!this.shipService.exists(shipId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            // getting ship from MySQL
            Ship shipGet = this.shipService.getAllById(shipId).get();
            if (shipGet == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        String name = ship.getName();
        String planet = ship.getPlanet();
        ShipType shipType = ship.getShipType();
        Date prodDate = ship.getProdDate();
        Double speed = ship.getSpeed();
        Integer crewSize = ship.getCrewSize();
        Boolean isUsed = ship.getUsed();
        Double rating = ship.getRating();
        if (name == null && planet == null && shipType == null &&
                prodDate == null && speed == null && crewSize == null && rating == null) {
        return  new ResponseEntity<>(shipGet, headers, HttpStatus.OK);
        }
        Boolean update = false;
        if (name != null) {
            if (name.isEmpty() || (name.length() > 50))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            shipGet.setName(name); update = true;
        }

        if (planet != null) {
            if (planet.isEmpty() || (planet.length() > 50))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            shipGet.setPlanet(planet);update = true;
        }

        shipType = ship.getShipType();
        if (shipType != null) {
            shipGet.setShipType(shipType);update = true;
        }
        if (prodDate != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            Date dateBefore = df.parse("2800");
            Date dateAfter = df.parse("3019");
            if (prodDate.getTime() < 0 || prodDate.before(dateBefore) || prodDate.after(dateAfter)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
                shipGet.setProdDate(prodDate);update = true;

        }
            if (isUsed != null) {
                shipGet.setUsed(isUsed); update = true;
            }

            if (speed != null) {
                if ((speed < 0.01) || (speed > 0.99))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                shipGet.setSpeed(speed);update = true;
            }

            if (crewSize != null) {
                if ((crewSize < 1) || (crewSize > 9999L))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                shipGet.setCrewSize(crewSize);update = true;
            }

            if (update) {
                Double R = shipGet.computR();
                shipGet.setRating(R);
                this.shipService.save(shipGet);
            }
            return new ResponseEntity<>(shipGet, headers, HttpStatus.OK);
        }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Ship> deleteShip(@PathVariable("id") Long shipId){
        if (shipId == null || shipId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!this.shipService.exists(shipId))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Ship ship = this.shipService.getAllById(shipId).get();
        if (ship == null )
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            this.shipService.delete(shipId);
            return new ResponseEntity<>(HttpStatus.OK);

    }
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
   // public ResponseEntity<List<Ship>> getAllShips(@RequestParam Map<String, String> params){
    public ResponseEntity<List<Ship>> getAllShips( @RequestParam(value = "before", required = false) Long before,
                                                   @RequestParam(value = "after", required = false) Long after,
                                                   @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value ="pageSize",  required = false) Integer pageSize,
                                                   @RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value ="planet", required = false) String planet,
                                                   @RequestParam(value = "shipType", required = false) ShipType shipType,
                                                   @RequestParam(value ="order", required = false)  ShipOrder order,
                                                   @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                                   @RequestParam(value ="maxSpeed", required = false) Double maxSpeed,
                                                   @RequestParam(value = "minCrewSize", required = false)Integer minCrewSize,
                                                   @RequestParam(value ="maxCrewSize", required = false) Integer maxCrewSize,
                                                   @RequestParam(value = "minRating", required = false) Double minRating,
                                                   @RequestParam(value ="maxRating", required = false) Double maxRating,
                                                   @RequestParam(value ="isUsed", required = false) Boolean isUsed){


             if ( pageNumber == null )  pageNumber = 0;
             if ( pageSize == null )    pageSize =3;
             if ( order == null )       order = ShipOrder.ID;

             if ( minSpeed == null )    minSpeed = 0.01;
             if ( maxSpeed == null )    maxSpeed = 0.99;
             if ( minCrewSize == null ) minCrewSize = 1;
             if ( maxCrewSize == null ) maxCrewSize = 9999;


        Specification<Ship> spec = where(betweenSpeed(minSpeed, maxSpeed));
        spec = spec.and(betweenCrewSize(minCrewSize,maxCrewSize));

        if ( minRating != null) {
            spec = spec.and(getGreaterRating(minRating));
        }
        if ( maxRating != null) {
            spec = spec.and(getLessRating(maxRating));
        }
        if (isUsed != null) {
            spec = spec.and(forIsUsed(isUsed));
        }
        if (name != null ){
            spec = spec.and(nameContains(name));
        }
        if ( planet != null ) {
            spec = spec.and(planetContains(planet));
        }
        if ( shipType != null ){
            spec = spec.and(forShipType(shipType));
        }
        if ( before != null) {
            spec = spec.and(getBefore(new Date(before)));
        }
        if (after != null) {
            spec = spec.and(getAfter(new Date(after)));
        }

        PageRequest pr = PageRequest.of(pageNumber,pageSize, Sort.by(order.getFieldName()));
        List<Ship> ships = this.shipService.getAll(spec,pr);

        if ( ships.isEmpty() ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ships, HttpStatus.OK);
    }
    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<Long> getCountShips( @RequestParam(value = "before", required = false) Long before,
                                                   @RequestParam(value = "after", required = false) Long after,
                                                   @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value ="pageSize",  required = false) Integer pageSize,
                                                   @RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value ="planet", required = false) String planet,
                                                   @RequestParam(value = "shipType", required = false) ShipType shipType,
                                                   @RequestParam(value ="order", required = false)  ShipOrder order,
                                                   @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                                   @RequestParam(value ="maxSpeed", required = false) Double maxSpeed,
                                                   @RequestParam(value = "minCrewSize", required = false)Integer minCrewSize,
                                                   @RequestParam(value ="maxCrewSize", required = false) Integer maxCrewSize,
                                                   @RequestParam(value = "minRating", required = false) Double minRating,
                                                   @RequestParam(value ="maxRating", required = false) Double maxRating,
                                                   @RequestParam(value ="isUsed", required = false) Boolean isUsed){

        Long count = 0L;
        if ( pageNumber == null )  pageNumber = 0;
        if ( pageSize == null )    pageSize =3;
        if ( order == null )       order = ShipOrder.ID;

        if ( minSpeed == null )    minSpeed = 0.01;
        if ( maxSpeed == null )    maxSpeed = 0.99;
        if ( minCrewSize == null ) minCrewSize = 1;
        if ( maxCrewSize == null ) maxCrewSize = 9999;


        Specification<Ship> spec = where(betweenSpeed(minSpeed, maxSpeed));
        spec = spec.and(betweenCrewSize(minCrewSize,maxCrewSize));

        if ( minRating != null) {
            spec = spec.and(getGreaterRating(minRating));
        }
        if ( maxRating != null) {
            spec = spec.and(getLessRating(maxRating));
        }
        if (isUsed != null) {
            spec = spec.and(forIsUsed(isUsed));
        }
        if (name != null ){
            spec = spec.and(nameContains(name));
        }
        if ( planet != null ) {
            spec = spec.and(planetContains(planet));
        }
        if ( shipType != null ){
            spec = spec.and(forShipType(shipType));
        }
        if ( before != null) {
            spec = spec.and(getBefore(new Date(before)));
        }
        if (after != null) {
            spec = spec.and(getAfter(new Date(after)));
        }
        count = this.shipService.count(spec);

        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
