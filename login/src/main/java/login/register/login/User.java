package login.register.login;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chandras")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username")
    private String username;

    @Column(name="encrypted_password")
    private String encrypted_password;

    @Column(name="encryptionKey")
    private String encryptionKey;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
