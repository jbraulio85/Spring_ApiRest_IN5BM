package com.in5bm.soccerfieldmanager.models;

import java.io.Serializable;
import java.sql.Date;

import com.in5bm.soccerfieldmanager.utils.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="reservation")
public class Reservation implements Serializable{
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Date startTime;
    private Date endTime;
    private String payment;

    private Status status;

    @ManyToOne
    private User user;

    @ManyToOne
    private SoccerField soccerField;
}
