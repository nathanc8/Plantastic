package com.plantastic.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (unique = true, nullable= false)
    private String pseudo;

    @Column (unique = true, nullable= false)
    private String email;

    @Column (nullable= false)
    private String password;

    @Column
    private Role role;

    @Column (nullable = false)
    private Date createdAt;

    @Column (nullable = false)
    private Date updatedAt;

    @Column (nullable = false)
    private NotificationsPreferences notificationsPreferences;

    @Column
    private LocalTime notificationsTime;

    @Column (nullable = false)
    private boolean notificationsConsent;

    @Column
    private boolean cameraConsent;

}
