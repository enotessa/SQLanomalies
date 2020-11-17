package com.enotessa.SQLanomalies.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Pet")
public class Pet {
    private Integer id_pet;
    private String name_pet;
    private String birth_date; //TODO формат даты
    private String type_pet;
    private String species;
    private String gender;
    //private Integer id_owner;

    private Owner owner;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_owner", nullable = false)
    public Owner getOwner() {
        return owner;
    }
    public void setOwner(Owner owner) {
        this.owner = owner;
    }


    private Set<Appointment> appointment;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pet")//targetEntity=com.enotessa.SQLanomalies.entities.Appointment.class)
    public Set<Appointment> getAppointment() {
        return appointment;
    }
    public void setAppointment(Set<Appointment> appointment) {
        this.appointment = appointment;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId_pet() {
        return id_pet;
    }

    public void setId_pet(Integer id_pet) {
        this.id_pet = id_pet;
    }

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getType_pet() {
        return type_pet;
    }

    public void setType_pet(String type_pet) {
        this.type_pet = type_pet;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    @Override
    public String toString() {
        return "Pet{" +
                "id_pet=" + id_pet +
                ", name_pet='" + name_pet + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", type_pet='" + type_pet + '\'' +
                ", species='" + species + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
