package sandromoretti.ragnarokspringapi.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

/*
    DROP TABLE IF EXISTS user_action_token;

    CREATE TABLE user_action_token(
        token varchar(64) primary key,
        user_id INT(11) UNSIGNED,
        created_at datetime not null,
        expires_at datetime not null,
        action varchar(25) not null,
        used bit(1) default 0
    )  ENGINE=MyISAM;

    ALTER TABLE user_action_token ADD CONSTRAINT fk_user_action_token FOREIGN KEY (user_id) REFERENCES login(account_id);
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserActionToken {
    public static final String ACTION_TOKEN_PASSWORD_RESET = "PASSWORD_RESET";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String token;

    @ManyToOne()
    @JoinColumn(name="user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name="created_at", updatable = false, nullable = false)
    private Timestamp created_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp expires_at;

    private String action;

    private boolean used;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public Timestamp getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Timestamp expires_at) {
        this.expires_at = expires_at;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
