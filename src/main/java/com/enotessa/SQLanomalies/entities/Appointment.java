package com.enotessa.SQLanomalies.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Appointment")
public class Appointment {
    private Integer id_appointment;
    private String date_app; //TODO date

    @ManyToMany(mappedBy = "appointments")
    private Set<Diagnosis> diagnoses = new HashSet<>();

    private Doctor doctor;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_doctor", nullable = false)
    public Doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    private Pet pet;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_pet", nullable = false)
    public Pet getPet() {
        return pet;
    }
    public void setPet(Pet pet) {
        this.pet = pet;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId_appointment() {
        return id_appointment;
    }

    public void setId_appointment(Integer id_appointment) {
        this.id_appointment = id_appointment;
    }

    public String getDate_app() {
        return date_app;
    }

    public void setDate_app(String date_app) {
        this.date_app = date_app;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id_appointment=" + id_appointment +
                ", date_app='" + date_app + '\'' +
                '}';
    }
}
