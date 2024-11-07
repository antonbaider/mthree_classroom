package com.mthree.bankmthree.entity;

import com.mthree.bankmthree.entity.enums.Role;
import com.mthree.bankmthree.entity.enums.Status;
import com.mthree.bankmthree.entity.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Entity class representing a User in the banking application.
 * This class maps to the "users" table in the database and defines
 * all necessary fields, constraints, and relationships associated
 * with a user.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    /**
     * Primary key for the User entity.
     * Automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User's first name.
     * Cannot be null or blank.
     */
    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;

    /**
     * User's last name.
     * Cannot be null or blank.
     */
    @NotBlank(message = "Last name is required")
    @Column(nullable = false)
    private String lastName;

    /**
     * User's unique username.
     * Must be unique and cannot be null or blank.
     */
    @NotBlank(message = "Username is required")
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * User's password.
     * Cannot be null or blank.
     * Note: Ensure that passwords are securely hashed before persisting.
     */
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    /**
     * User's unique email address.
     * Must be a valid email format, unique, and cannot be null or blank.
     */
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * User's phone number.
     * Must consist of 10 to 15 digits.
     * Must be unique if provided.
     */
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits")
    @Column(unique = true)
    private String phone;

    /**
     * Role assigned to the user.
     * Determines the user's permissions and access levels within the application.
     * Stored as a string representation of the enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Timestamp indicating when the user was created.
     * Automatically populated upon entity creation.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    /**
     * Timestamp indicating the last time the user was modified.
     * Automatically updated whenever the entity is changed.
     */
    @LastModifiedDate
    private LocalDateTime updateDate;

    /**
     * Current status of the user (e.g., ACTIVE, INACTIVE).
     * Stored as a string representation of the enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    /**
     * Type of user (e.g., CUSTOMER, ADMIN).
     * Stored as a string representation of the enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;

    /**
     * User's Social Security Number (SSN).
     * Must be unique and cannot be null or blank.
     * Excluded from generated toString() and equals/hashCode() methods for security reasons.
     */
    @NotBlank(message = "SSN is required")
    @Column(name = "ssn", nullable = false, unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private String ssn;

    /**
     * Set of family members associated with the user.
     * Represents a many-to-many self-referential relationship.
     * Use a join table named "user_family" to manage associations.
     * Excluded from generated toString() and equals/hashCode() methods to prevent potential recursion issues.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_family", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "family_member_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> family;

    /**
     * Set of groups that the user belongs to.
     * Represents a many-to-many relationship with the UserGroup entity.
     * Use a join table named "user_group" to manage associations.
     * Excluded from generated toString() methods to prevent potential recursion issues.
     */
    @ManyToMany
    @JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    @ToString.Exclude
    private Set<UserGroup> userGroups;

    /**
     * Set of transactions where the user is the sender.
     * Represents a one-to-many relationship with the Transaction entity.
     * Mapped by the "sender" field in the Transaction entity.
     * Excluded from generated toString() methods to prevent potential recursion issues.
     */
    @OneToMany(mappedBy = "sender")
    @ToString.Exclude
    private Set<Transaction> sentTransactions;

    /**
     * Set of transactions where the user is the receiver.
     * Represents a one-to-many relationship with the Transaction entity.
     * Mapped by the "receiver" field in the Transaction entity.
     * Excluded from generated toString() methods to prevent potential recursion issues.
     */
    @OneToMany(mappedBy = "receiver")
    @ToString.Exclude
    private Set<Transaction> receivedTransactions;

    /**
     * Set of accounts owned by the user.
     * Represents a one-to-many relationship with the Account entity.
     * Mapped by the "user" field in the Account entity.
     * CascadeType.ALL ensures that all account-related operations are propagated to the accounts.
     * FetchType.EAGER loads accounts immediately with the user entity.
     * Excluded from generated toString() and equals/hashCode() methods to prevent potential recursion issues.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Account> accounts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}