package com.enotessa.SQLanomalies.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Diagnosis")
public class Diagnosis {
    private Integer id_diagnosis;
    private String name_dia;
    private String description_dia;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "app_dia",
            joinColumns = { @JoinColumn(name = "id_diagnosis") },
            inverseJoinColumns = { @JoinColumn(name = "id_appointment") }
    )
    Set<Appointment> appointments = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "dia_th",
            joinColumns = { @JoinColumn(name = "id_diagnosis") },
            inverseJoinColumns = { @JoinColumn(name = "id_therapy") }
    )
    Set<Therapy> therapies = new HashSet<>();





    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId_diagnosis() {
        return id_diagnosis;
    }

    public void setId_diagnosis(Integer id_diagnosis) {
        this.id_diagnosis = id_diagnosis;
    }

    public String getName_dia() {
        return name_dia;
    }

    public void setName_dia(String name_dia) {
        this.name_dia = name_dia;
    }

    public String getDescription_dia() {
        return description_dia;
    }

    public void setDescription_dia(String description_dia) {
        this.description_dia = description_dia;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "id_diagnosis=" + id_diagnosis +
                ", name_dia='" + name_dia + '\'' +
                ", description_dia='" + description_dia + '\'' +
                '}';
    }
}
