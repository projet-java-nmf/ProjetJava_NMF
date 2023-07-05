package com.wcs.Security.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    private String firstname;
    private String lastname;

    @Column(
            unique = true,
            nullable = false
    )
    private String email;
    private String password;

    //JOINTURE MANY TO MANY SIMPLE USER & ROLE
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(
                    name = "user_roles",
                    joinColumns = {
                            @JoinColumn(name = "user_id")
                     },inverseJoinColumns = {
                            @JoinColumn(name = "role_id")
                     }
            )
    List<Role> roles = new ArrayList<>();

    //JOINTURE USER & VIDEO
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<UserVideo> favoritesList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        roles.forEach(role -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().name()));
        });

        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
