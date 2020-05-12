package com.greenapp.authservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PLAIN_USERS")
public class PlainUser {

    @Id
    @GeneratedValue
    private BigInteger id;
    private String _2faCode;
    @JsonIgnore
    private Enum<TwoFaTypes> _2faDefaultType;
    private Timestamp _2faExpireTime;
    private boolean _is2faEnabled;
    private Date birthDate;
    private String firstName;
    private boolean isEnabled;
    private String lastName;
    private String mailAddress;
    private String phoneNumber;
    private Timestamp registeredDate;
    private String sessionToken;
    private boolean isOnline;
    private String password;
}
