package com.space.model;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "ship")
public class Ship extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "planet")
    private String planet;

    @Column(name = "shipType")
    @Enumerated(EnumType.STRING)
    private ShipType shipType;


    @Column(name = "prodDate")
    private Date prodDate;

    @Column(name = "isUsed")
    private Boolean isUsed = false;


    @Column(name = "speed")
    private Double speed;

    @Column(name = "crewSize")
    private Integer crewSize;

    @Column(name = "rating")
    private Double rating;



    public String getName() {
        return name;
        }

    public void setName(String name) {
        this.name = name;
        }

    public String getPlanet() {
            return planet;
        }

    public void setPlanet(String planet) {
            this.planet = planet;
        }

    public ShipType getShipType() {
            return shipType;
        }

    public void setShipType(ShipType shipType) {
            this.shipType = shipType;
        }

     public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }
    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double computR() throws ParseException {
    Double R = isUsed == false ? 1 : 0.5;

    SimpleDateFormat df1 = new SimpleDateFormat("yyyy");
    String s = df1.format(prodDate);
    Integer date = Integer.parseInt(s);
    R = R*80*speed / (3019 - date +1);
    R =   (double)Math.round(R * 100d) / 100d;
    return R;
    }


}
