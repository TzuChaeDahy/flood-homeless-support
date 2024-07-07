package com.tzuchaedahy.domain;

import com.tzuchaedahy.exceptions.DomainException;
import com.tzuchaedahy.util.Regex;

import java.util.Objects;
import java.util.UUID;

public class Shelter {
    private UUID id;
    private String name;
    private String address;
    private String responsible;
    private String phone;
    private String email;
    private Integer capacity;
    private Integer occupation;

    public Shelter() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.isEmpty() || name.isBlank()) {
            throw new DomainException("shelter name cannot be empty.");
        }

        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address.isEmpty() || address.isBlank()) {
            throw new DomainException("shelter address cannot be empty.");
        }

        this.address = address;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        if (responsible.isEmpty() || responsible.isBlank()) {
            throw new DomainException("shelter responsible name cannot be empty.");
        }

        this.responsible = responsible;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (!Regex.isPhoneValid(phone)) {
            throw new DomainException("shelter phone number is not valid.");
        }

        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!Regex.isEmailValid(email)) {
            throw new DomainException("shelter email is not valid.");
        }

        this.email = email;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        if (capacity < 1) {
            throw new DomainException("capacity cannot be less than one.");
        }

        this.capacity = capacity;
    }

    public Integer getOccupation() {
        return occupation;
    }

    public void setOccupation(Integer occupation) {
        if (occupation < 0) {
            throw new DomainException("occupation cannot be less than one.");
        }

        this.occupation = occupation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelter shelter = (Shelter) o;
        return Objects.equals(id, shelter.id) && Objects.equals(name, shelter.name) && Objects.equals(address, shelter.address) && Objects.equals(responsible, shelter.responsible) && Objects.equals(phone, shelter.phone) && Objects.equals(email, shelter.email) && Objects.equals(capacity, shelter.capacity) && Objects.equals(occupation, shelter.occupation);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(address);
        result = 31 * result + Objects.hashCode(responsible);
        result = 31 * result + Objects.hashCode(phone);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(capacity);
        result = 31 * result + Objects.hashCode(occupation);
        return result;
    }
}
