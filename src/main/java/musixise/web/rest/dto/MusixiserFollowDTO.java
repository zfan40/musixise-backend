package musixise.web.rest.dto;

import musixise.domain.User;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the MusixiserFollow entity.
 */
public class MusixiserFollowDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MusixiserFollowDTO musixiserFollowDTO = (MusixiserFollowDTO) o;

        if ( ! Objects.equals(id, musixiserFollowDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MusixiserFollowDTO{" +
            "id=" + id +
            ", userId=" + userId +
            ", user=" + user +
            '}';
    }
}
