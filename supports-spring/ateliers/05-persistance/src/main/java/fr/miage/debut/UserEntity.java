package fr.miage.debut;
import jakarta.persistence.*;
@Entity
@Table(name="app_users")
public class UserEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    protected UserEntity() {}
    public UserEntity(String name) { this.name = name; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
