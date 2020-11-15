package com.enotessa.SQLanomalies.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Dia_th")
@IdClass(Dia_th_PK.class)
public class Dia_th implements Serializable {
    private Integer duration;
    private Float cost_RUB;

    private Diagnosis diagnosis;
    @Id
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_diagnosis", nullable = false)
    public Diagnosis getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    private Therapy therapy;
    @Id
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_therapy", nullable = false)
    public Therapy getTherapy() {
        return therapy;
    }
    public void setTherapy(Therapy therapy) {
        this.therapy = therapy;
    }



    /*@Id
    @AttributeOverrides({
            @AttributeOverride(name = "id_diagnosis",
                    column = @Column(name="id_diagnosis")),
            @AttributeOverride(name = "id_therapy",
                    column = @Column(name="id_therapy"))
    })*/

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Float getCost_RUB() {
        return cost_RUB;
    }

    public void setCost_RUB(Float cost_RUB) {
        this.cost_RUB = cost_RUB;
    }
}

class Dia_th_PK implements Serializable {
    private Integer id_diagnosis;
    private Integer id_therapy;

}
