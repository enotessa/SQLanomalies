package com.enotessa.SQLanomalies.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "app_dia")
public class App_dia implements Serializable {

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

    private Appointment appointment;
    @Id
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_appointment", nullable = false)
    public Appointment getAppointment() {
        return appointment;
    }
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

}
