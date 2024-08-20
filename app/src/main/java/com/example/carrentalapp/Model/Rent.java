package com.example.carrentalapp.Model;


import java.io.Serializable;
import java.util.Date;

public class Rent implements Serializable {
    private String rentid;

    private Double price;

    private Date begindate;

    private Date returndate;

    private Integer rentflag;

    private String identity;

    private String carnumber;

    private String opername;

    private Date createtime;

    public Rent() {
    }

    public Rent(String rentid, Double price, Date begindate, Date returndate, Integer rentflag, String identity, String carnumber, String opername, Date createtime) {
        this.rentid = rentid;
        this.price = price;
        this.begindate = begindate;
        this.returndate = returndate;
        this.rentflag = rentflag;
        this.identity = identity;
        this.carnumber = carnumber;
        this.opername = opername;
        this.createtime = createtime;
    }

    public String getRentid() {
        return rentid;
    }

    public void setRentid(String rentid) {
        this.rentid = rentid == null ? null : rentid.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public Date getReturndate() {
        return returndate;
    }

    public void setReturndate(Date returndate) {
        this.returndate = returndate;
    }

    public Integer getRentflag() {
        return rentflag;
    }

    public void setRentflag(Integer rentflag) {
        this.rentflag = rentflag;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity == null ? null : identity.trim();
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber == null ? null : carnumber.trim();
    }

    public String getOpername() {
        return opername;
    }

    public void setOpername(String opername) {
        this.opername = opername == null ? null : opername.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "rentid='" + rentid + '\'' +
                ", price=" + price +
                ", begindate=" + begindate +
                ", returndate=" + returndate +
                ", rentflag=" + rentflag +
                ", identity='" + identity + '\'' +
                ", carnumber='" + carnumber + '\'' +
                ", opername='" + opername + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}