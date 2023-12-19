package sandromoretti.ragnarokspringapi.request;

import com.mercadopago.resources.payment.Payment;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Validated
public class MercadoPagoWebHookNotification {
    @NotNull
    @Positive
    private long id;
    private boolean live_mode;
    @NotBlank
    private String type;
    private Date date_created;
    private long user_id;
    private String api_version;
    @NotBlank
    private String action;
    private DataObject data;

    @Override
    public String toString(){
        return id+"/"+(live_mode?"production":"sandbox")+"/"+type+"/"+action+"/"+data;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isLive_mode() {
        return live_mode;
    }

    public void setLive_mode(boolean live_mode) {
        this.live_mode = live_mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getApi_version() {
        return api_version;
    }

    public void setApi_version(String api_version) {
        this.api_version = api_version;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getDataId(){
        return this.getData().getId();
    }
    public DataObject getData() {
        return data;
    }

    public void setData(DataObject data) {
        this.data = data;
    }
}

class DataObject{
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return id+"";
    }
}