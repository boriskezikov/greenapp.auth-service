package com.greenapp.authservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PLAIN_USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "two_fa_code")
    private String _2faCode;
    @JsonIgnore
    private Enum<TwoFaTypes> _2faDefaultType;

    @Column(name = "two_fa_code_expire_time")
    private Timestamp _2faExpireTime;

    @Column(name = "is_two_fa_enabled")
    private boolean _is2faEnabled;

    @Column
    private Date birthDate;
    @Column
    private String firstName;
    @Column(name = "is_enabled")
    private boolean isEnabled;
    @Column
    private String lastName;
    @Column
    private String mailAddress;
    private transient String phoneNumber;
    @Column
    private Timestamp registeredDate;
    @Column
    @Type(type = "text")
    private String sessionToken;
    private boolean isOnline;
    @Column
    private String password;

    public void setRegisteredDate(){
        this.registeredDate = Timestamp.valueOf(LocalDateTime.now());
    }
}
