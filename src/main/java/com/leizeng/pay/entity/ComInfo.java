package com.leizeng.pay.entity;

/**
 * @author cloudy
 * @version 1.0
 * @date 18/1/29 下午5:54
 */
public class ComInfo {
    private Integer id;
    private String companyName;
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ComInfo [id=" + id
                + ",companyName=" + companyName
                + ",address=" + address
                + "]";
    }
}
