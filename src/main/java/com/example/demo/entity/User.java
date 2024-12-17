package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'NORMAL'")
    private UserStatus status = UserStatus.NORMAL; // NORMAL, BLOCKED

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'USER'")
    private Role role = Role.USER;

    public User(String role, String email, String nickname, String password) {
        this.role = Role.of(role);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public User() {}

    public void updateStatusToBlocked() {
        this.status = UserStatus.BLOCKED;
    }


}
