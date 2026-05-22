import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static final String DIR_ARRIBADA = "C:\\tmp\\";

    private Socket socket;

    private ObjectOutputStream sortida;
    private ObjectInputStream entrada;

    public void connectar() throws IOException {

        System.out.println("Connectant a -> "
                + Servidor.HOST + ":" + Servidor.PORT);

        socket = new Socket(Servidor.HOST, Servidor.PORT);

        System.out.println(
                "Connexio acceptada: " + socket.getInetAddress());

        sortida = new ObjectOutputStream(socket.getOutputStream());
        entrada = new ObjectInputStream(socket.getInputStream());
    }

    public void rebreFitxers() {

        Scanner scanner = new Scanner(System.in);

        try {

            while (true) {

                System.out.print(
                        "Nom del fitxer a rebre ('sortir' per sortir): ");

                String nomFitxer = scanner.nextLine();

                sortida.writeObject(nomFitxer);
                sortida.flush();

                if (nomFitxer.equalsIgnoreCase("sortir")) {
                    System.out.println("Sortint...");
                    break;
                }

                byte[] contingut = (byte[]) entrada.readObject();

                if (contingut.length == 0) {
                    System.out.println("Fitxer buit o inexistent.");
                    continue;
                }

                File fitxerOriginal = new File(nomFitxer);

                String nomGuardar =
                        DIR_ARRIBADA + fitxerOriginal.getName();

                System.out.println(
                        "Nom del fitxer a guardar: " + nomGuardar);

                FileOutputStream fos =
                        new FileOutputStream(nomGuardar);

                fos.write(contingut);

                fos.close();

                System.out.println(
                        "Fitxer rebut i guardat com: "
                                + nomGuardar);
            }

            scanner.close();

        } catch (Exception e) {
            System.out.println("Error al client: " + e.getMessage());
        }
    }

    public void tancarConnexio() {

        try {

            if (entrada != null) {
                entrada.close();
            }

            if (sortida != null) {
                sortida.close();
            }

            if (socket != null) {
                socket.close();
            }

            System.out.println("Connexio tancada.");

        } catch (IOException e) {
            System.out.println("Error tancant connexió");
        }
    }

    public static void main(String[] args) {

        Client client = new Client();

        try {

            client.connectar();

            client.rebreFitxers();

            client.tancarConnexio();

        } catch (IOException e) {
            System.out.println("Error al client");
        }
    }
}