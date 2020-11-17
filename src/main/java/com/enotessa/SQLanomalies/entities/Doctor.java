package com.enotessa.SQLanomalies.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Doctor")
public class Doctor {
    @Column(name = "id_doctor")
    private Integer id_doctor;
    @Column(name = "lname_d")
    private String lname_d;
    @Column(name = "name_d")
    private String name_d;
    @Column(name = "patron_d")
    private String patron_d;
    @Column(name = "N_cabinet")
    private Integer N_cabinet;
    @Column(name = "tel_d")
    private String tel_d;


    private Set<Appointment> appointment;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "doctor")//targetEntity=com.enotessa.SQLanomalies.entities.Appointment.class)
    public Set<Appointment> getAppointment() {
        return appointment;
    }

    public void setAppointment(Set<Appointment> appointment) {
        this.appointment = appointment;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(Integer id_doctor) {
        this.id_doctor = id_doctor;
    }

    public String getLname_d() {
        return lname_d;
    }

    public void setLname_d(String lname_d) {
        this.lname_d = lname_d;
    }

    public String getName_d() {
        return name_d;
    }

    public void setName_d(String name_d) {
        this.name_d = name_d;
    }

    public String getPatron_d() {
        return patron_d;
    }

    public void setPatron_d(String patron_d) {
        this.patron_d = patron_d;
    }

    public Integer getN_cabinet() {
        return N_cabinet;
    }

    public void setN_cabinet(Integer n_cabinet) {
        N_cabinet = n_cabinet;
    }

    public String getTel_d() {
        return tel_d;
    }

    public void setTel_d(String tel_d) {
        this.tel_d = tel_d;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id_doctor=" + id_doctor +
                ", lname_d='" + lname_d + '\'' +
                ", name_d='" + name_d + '\'' +
                ", patron_d='" + patron_d + '\'' +
                ", N_cabinet=" + N_cabinet +
                ", tel_d='" + tel_d + '\'' +
                '}';
    }
}
