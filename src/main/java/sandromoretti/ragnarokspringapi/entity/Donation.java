package sandromoretti.ragnarokspringapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import sandromoretti.ragnarokspringapi.config.DonationConfig;

import java.sql.Timestamp;

// we can't use update hibernate function due this table. This table have to use MyISAM due to foreign key with login.
// if someone can update this code to auto create table using MyISAM is great. I tried and failed misably. PS: have to work in mysql and mariadb

/*
DROP TABLE donation;

CREATE TABLE donation(
    id int primary key auto_increment,
    user_id INT(11) UNSIGNED,
    service varchar(25),
    service_order_id varchar(255),
    created_at datetime,
    paid_at datetime,
    completed_at datetime,
    paid int(1),
    completed int(1),
    status varchar(25),
    amount float(11,2) NOT NULL,
    coupon varchar(25)
) ENGINE=MyISAM;

ALTER TABLE donation ADD CONSTRAINT fk_donation_user FOREIGN KEY (user_id) REFERENCES login(account_id);
 */

@Entity
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne()
    @JoinColumn(name="user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp paid_at;

    @Temporal(TemporalType.TIMESTAMP)
    private
    Timestamp completed_at;

    private int paid;
    private int completed;

    @NotBlank
    @NotNull
    private String service;

    @Column(name="service_order_id")
    private String serviceOrderId;

    private String status;

    @NotNull
    @Min(DonationConfig.MIN_DONATION_AMOUNT)
    private float amount;

    private String coupon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(Timestamp paid_at) {
        this.paid_at = paid_at;
    }

    public Timestamp getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(Timestamp completed_at) {
        this.completed_at = completed_at;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(String serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }
}
