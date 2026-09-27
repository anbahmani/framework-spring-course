package fr.miage.debut;
public class InvalidName extends RuntimeException {
    public InvalidName() { super("Le nom est obligatoire"); }
}
