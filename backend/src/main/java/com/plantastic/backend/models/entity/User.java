package com.plantastic.backend.models.entity;

import com.plantastic.backend.models.types.NotificationsPreferences;
import com.plantastic.backend.models.types.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @Enumerated(EnumType.STRING)
    @Column
    private UserRole role;

    @Column (name = "created_at",nullable = false)
    private LocalDate createdAt;

    @Column (name = "updated_at",nullable = false)
    private LocalDate updatedAt;

    @Enumerated(EnumType.STRING)
    @Column (name = "notifications_preferences",nullable = false)
    private NotificationsPreferences notificationsPreferences;

    @Column(name = "notifications_time")
    private LocalTime notificationsTime;

    @Column (name = "notifications_consent", nullable = false)
    private boolean notificationsConsent;

    @Column(name = "camera_consent")
    private boolean cameraConsent;

}
