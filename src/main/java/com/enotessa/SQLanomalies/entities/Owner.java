package com.enotessa.SQLanomalies.entities;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Owner")
public class Owner {
    private Integer id_owner;
    private String lname;
    private String name;
    private String patron;
    private String address;
    private String tel;


    private Set<Pet> pet;
    @OneToMany(fetch = FetchType.EAGER,  mappedBy = "owner")//targetEntity=com.enotessa.SQLanomalies.entities.Pet.class)
    public Set<Pet> getPet() {
        return pet;
    }
    public void setPet(Set<Pet> pet) {
        this.pet = pet;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId_owner() {
        return id_owner;
    }

    public void setId_owner(Integer id_owner) {
        this.id_owner = id_owner;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatron() {
        return patron;
    }

    public void setPatron(String patron) {
        this.patron = patron;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id_owner=" + id_owner +
                ", lname='" + lname + '\'' +
                ", name='" + name + '\'' +
                ", patron='" + patron + '\'' +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
