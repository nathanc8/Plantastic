package com.plantastic.backend.models.entity;

import com.plantastic.backend.models.types.NotificationsPreferences;
import com.plantastic.backend.models.types.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true, nullable= false)
    private String username;

    @Column (unique = true, nullable= false)
    private String email;

    @Column (nullable= false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRole role;

    @Column (name = "created_at",nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column (name = "updated_at",nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDate lastLoginAt;

    @Enumerated(EnumType.STRING)
    @Column (name = "notifications_preferences", nullable = true)
    private NotificationsPreferences notificationsPreferences;

    @Column(name = "notifications_time", columnDefinition = "TIME")
    private LocalTime notificationsTime;

    @Column (name = "notifications_consent", nullable = false)
    private boolean notificationsConsent;

    @Column(name = "camera_consent")
    private boolean cameraConsent;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserHome> userHomes = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserPlant> userPlants = new HashSet<>();

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
