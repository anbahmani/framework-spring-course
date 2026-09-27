package fr.miage.debut;
public class UserNotFound extends RuntimeException {
    public UserNotFound() { super("Utilisateur introuvable"); }
}
