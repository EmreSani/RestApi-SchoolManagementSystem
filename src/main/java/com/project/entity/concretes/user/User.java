package com.project.entity.concretes.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.entity.enums.Gender;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_user")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String ssn;

    private String name;

    private String surname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    private String birthPlace;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private Boolean built_in;

    private String motherName;

    private String fatherName;

    private int studentNumber;

    private boolean isActive;

    private Boolean isAdvisor;

    private Long advisorTeacherId; // bu Ogrenciler icin lazim, kendi rehber ogretmeninin
    // id si buraya yazilacak

    @Enumerated(EnumType.STRING)
    private Gender gender; // MALE , male , Male , MalE

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserRole userRole;

}
