package com.enotessa.SQLanomalies.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Therapy")
public class Therapy {
    private Integer id_therapy;
    private String description;

    @ManyToMany(mappedBy = "therapies")
    private Set<Diagnosis> diagnoses = new HashSet<>();


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId_therapy() {
        return id_therapy;
    }

    public void setId_therapy(Integer id_therapy) {
        this.id_therapy = id_therapy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Therapy{" +
                "id_therapy=" + id_therapy +
                ", description='" + description + '\'' +
                '}';
    }
}
