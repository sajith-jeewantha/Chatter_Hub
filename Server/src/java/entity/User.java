/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Entity;
import entity.User_Status;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author sajithjeewantha
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mobile", length = 10, nullable = false)
    private String mobile;

    @Column(name = "first_name", length = 45, nullable = false)
    private String first_name;

    @Column(name = "last_name", length = 45, nullable = false)
    private String last_name;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Column(name = "registered_date_time", nullable = false)
    private Date registered_date_time;
    
     @Column(name = "chat_time", nullable = false)
    private Date chatTime;

    @ManyToOne
    @JoinColumn(name = "user_status_id", nullable = false)
    private User_Status status;


    public User() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the registered_date_time
     */
    public Date getRegistered_date_time() {
        return registered_date_time;
    }

    /**
     * @param registered_date_time the registered_date_time to set
     */
    public void setRegistered_date_time(Date registered_date_time) {
        this.registered_date_time = registered_date_time;
    }

    /**
     * @return the status
     */
    public User_Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(User_Status status) {
        this.status = status;
    }

    /**
     * @return the chatTime
     */
    public Date getChatTime() {
        return chatTime;
    }

    /**
     * @param chatTime the chatTime to set
     */
    public void setChatTime(Date chatTime) {
        this.chatTime = chatTime;
    }

   

}
