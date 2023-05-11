package com.pragma.powerup.usermicroservice.domain.model;

public class RestaurantModel {
    private Long id;
    private String name;
    private String address;
    private String idOwner;
    private String phone;
    private String urlLogo;
    private String nit;

    public RestaurantModel(Long id, String name, String address, String idOwner, String phone, String urlLogo, String nit) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.idOwner = idOwner;
        this.phone = phone;
        this.urlLogo = urlLogo;
        this.nit = nit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }
}
