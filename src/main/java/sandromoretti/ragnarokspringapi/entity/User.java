package sandromoretti.ragnarokspringapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name="login")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer account_id;

    @NotBlank(message = "Informe um e-mail valido")
    @Column(name="email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Informe um login valido")
    @Column(name="userid", nullable = false, unique = true)
    private String userid;

    @NotBlank(message = "Informe uma senha valida")
    @Size(min=8, max=25)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String user_pass;

    private char sex;

    @Column(columnDefinition = "TINYINT")
    private int group_id;

    @OneToMany(mappedBy = "user")
    private List<Donation> donations;

    public Integer getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Integer account_id) {
        this.account_id = account_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    @Override
    public String toString(){
        return this.getAccount_id() + "/" + this.getUserid();
    }
}
