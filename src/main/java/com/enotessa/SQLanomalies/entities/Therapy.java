package com.enotessa.SQLanomalies.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Therapy")
public class Therapy {
    private Integer id_therapy;
    private String description;


    private Set<Dia_th> dia_ths;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "therapy")//targetEntity=com.enotessa.SQLanomalies.entities.Dia_th.class)
    public Set<Dia_th> getDia_ths() {
        return dia_ths;
    }

    public void setDia_ths(Set<Dia_th> dia_ths) {
        this.dia_ths = dia_ths;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
