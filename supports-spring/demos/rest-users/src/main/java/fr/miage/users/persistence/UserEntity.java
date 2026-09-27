package fr.miage.users.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 254)
    private String email;
    protected UserEntity() {}
    public UserEntity(String name, String email) { rename(name, email); }
    public void rename(String name, String email) { this.name = name; this.email = email; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}
