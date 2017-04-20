package com.ostdlabs.etoyataxi.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "appuser")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private long id;
    
    private String username;

    private String usernameCanonical;

    private String email;

    @Column(nullable = true)
    private String emailCanonical;

    private Boolean enabled = true;

    @Column(nullable = true)
    private String salt;

    private String passwordHash;

    @Column(nullable = true)
    private Timestamp lastLogin;

    @Column(nullable = true)
    private String confirmationToken;

    @Column(nullable = true)
    private Timestamp passwordRequestedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserGroup> groups;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_id", nullable = true, updatable = true) },
            inverseJoinColumns = { @JoinColumn(name = "roles_id",
                    nullable = true, updatable = true) })
    private Set<Role> roles;

    private Boolean locked = false;

    private Boolean expired = false;

    @Column(nullable = true)
    private Timestamp expiresAt;

    private Boolean credentialsExpired = false;

    @Column(nullable = true)
    private Timestamp credentialsExpiredAt;

    private int balance = 0;

    private int balanceBonus = 0;

    private int balanceTotal = 0;

    @Column(nullable = true)
    private Timestamp lastPayment;

    @Column(nullable = true)
    private String site;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private String company;

    @Column(nullable = true)
    private String name;

    @Transient
    private final Set<GrantedAuthority> authorities = new HashSet<>();

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernameCanonical() {
        return usernameCanonical;
    }

    public void setUsernameCanonical(String usernameCanonical) {
        this.usernameCanonical = usernameCanonical;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailCanonical() {
        return emailCanonical;
    }

    public void setEmailCanonical(String emailCanonical) {
        this.emailCanonical = emailCanonical;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Timestamp getPasswordRequestedAt() {
        return passwordRequestedAt;
    }

    public void setPasswordRequestedAt(Timestamp passwordRequestedAt) {
        this.passwordRequestedAt = passwordRequestedAt;
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = groups;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean isExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Timestamp getCredentialsExpiredAt() {
        return credentialsExpiredAt;
    }

    public void setCredentialsExpiredAt(Timestamp credentialsExpiredAt) {
        this.credentialsExpiredAt = credentialsExpiredAt;
    }

/*    public Set<Banner> getBanners() {
        return banners;
    }

    public void setBanners(Set<Banner> banners) {
        this.banners = banners;
    }*/

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Timestamp getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(Timestamp lastPayment) {
        this.lastPayment = lastPayment;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalanceBonus() {
        return balanceBonus;
    }

    public void setBalanceBonus(int balanceBonus) {
        this.balanceBonus = balanceBonus;
    }

    public int getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(int balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
