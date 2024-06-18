package com.akshit.treading.modal;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Jwt;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class TwoFactorOTP {

    @Id
    private String id;

    private String otp;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @OneToOne
    private User user;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String jwt;

}
