package fr.miage.users.domain;

public class UserNotFound extends RuntimeException {
    public UserNotFound(long id) { super("Utilisateur " + id + " introuvable"); }
}
