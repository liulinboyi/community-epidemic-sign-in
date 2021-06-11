package com.companysign.po;

/**
 * 社区出入登记系统
 */
public class Registration {
    private Integer id; // id
    private String username; // 姓名
    //    private String passwd;  // 账户密码
    private String home; // 住所
    private String phone; // 手机号码
    private String come; // 从哪里来
    private Boolean danger; // 是否去过高风险地区

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCome() {
        return come;
    }

    public void setCome(String come) {
        this.come = come;
    }

    public Boolean getDanger() {
        return danger;
    }

    public void setDanger(Boolean danger) {
        this.danger = danger;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", home='" + home + '\'' +
                ", phone='" + phone + '\'' +
                ", come='" + come + '\'' +
                ", danger=" + danger +
                '}';
    }
}
