package com.vtech.app.data.bean;

public class ContactData {
    private String name;

    private String telephone;

    private String contact_id;

    public ContactData() {
    }

    public ContactData(String name, String telephone, String contact_id) {
        this.name = name;
        this.telephone = telephone;
        this.contact_id = contact_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }
}
