package vulnspa.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // <-- вот оно
    @Column(name = "id")
    private Long id;
    private String username;
    private String password;
    private boolean isVip;
    private boolean enabled = true;

    // Геттеры и сеттеры нужны для JSON сериализации


    public boolean isVip() {
        return isVip;
    }

    public void setIsVip(boolean vip) {
        isVip = vip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
