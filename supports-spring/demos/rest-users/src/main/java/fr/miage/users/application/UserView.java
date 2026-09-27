package fr.miage.users.application;

import fr.miage.users.persistence.UserEntity;

public record UserView(long id, String name, String email) {
    public static UserView from(UserEntity user) {
        return new UserView(user.getId(), user.getName(), user.getEmail());
    }
}
