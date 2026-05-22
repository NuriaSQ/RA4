import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Fitxer {

    private String nom;
    private byte[] contingut;

    public Fitxer(String nom) {
        this.nom = nom;
    }

    public byte[] getContingut() throws IOException {

        File fitxer = new File(nom);

        if (!fitxer.exists()) {
            throw new IOException("El fitxer no existeix");
        }

        contingut = Files.readAllBytes(fitxer.toPath());

        return contingut;
    }
}