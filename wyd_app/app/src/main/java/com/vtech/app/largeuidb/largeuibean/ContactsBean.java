package com.vtech.app.largeuidb.largeuibean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ContactsBean {
    @Id(autoincrement = true)
    private Long Id;
    private int type;
    private String number;
    private String name;

    @Generated(hash = 575686249)
    public ContactsBean(Long Id, int type, String number, String name) {
        this.Id = Id;
        this.type = type;
        this.number = number;
        this.name = name;
    }

    @Generated(hash = 747317112)
    public ContactsBean() {
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
