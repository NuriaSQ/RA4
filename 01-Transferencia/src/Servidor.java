import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final int PORT = 9999;
    public static final String HOST = "localhost";

    private ServerSocket serverSocket;
    private Socket socket;

    private ObjectOutputStream sortida;
    private ObjectInputStream entrada;

    public Socket connectar() throws IOException {

        serverSocket = new ServerSocket(PORT);

        System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
        System.out.println("Esperant connexio...");

        socket = serverSocket.accept();

        System.out.println("Connexio acceptada: " + socket.getInetAddress());

        sortida = new ObjectOutputStream(socket.getOutputStream());
        entrada = new ObjectInputStream(socket.getInputStream());

        return socket;
    }

    public void enviarFitxers() {

        try {

            while (true) {

                System.out.println("Esperant el nom del fitxer del client...");

                String nomFitxer = (String) entrada.readObject();

                System.out.println("Nomfitxer rebut: " + nomFitxer);

                if (nomFitxer == null || nomFitxer.isEmpty()
                        || nomFitxer.equalsIgnoreCase("sortir")) {

                    System.out.println("Nom del fitxer buit o nul. Sortint...");
                    break;
                }

                try {

                    Fitxer fitxer = new Fitxer(nomFitxer);

                    byte[] contingut = fitxer.getContingut();

                    System.out.println(
                            "Contigut del fitxer a enviar: "
                                    + contingut.length + " bytes");

                    sortida.writeObject(contingut);
                    sortida.flush();

                    System.out.println(
                            "Fitxer enviat al client: " + nomFitxer);

                } catch (IOException e) {

                    System.out.println(
                            "Error llegint el fitxer del client: "
                                    + e.getMessage());

                    sortida.writeObject(new byte[0]);
                    sortida.flush();
                }
            }

        } catch (Exception e) {
            System.out.println("Error al servidor: " + e.getMessage());
        }
    }

    public void tancarConnexio(Socket socket) {

        try {

            if (entrada != null) {
                entrada.close();
            }

            if (sortida != null) {
                sortida.close();
            }

            if (socket != null) {
                System.out.println(
                        "Tancant connexió amb el client: "
                                + socket.getInetAddress());
                socket.close();
            }

            if (serverSocket != null) {
                serverSocket.close();
            }

        } catch (IOException e) {
            System.out.println("Error tancant connexions");
        }
    }

    public static void main(String[] args) {

        Servidor servidor = new Servidor();

        try {

            Socket socket = servidor.connectar();

            servidor.enviarFitxers();

            servidor.tancarConnexio(socket);

        } catch (IOException e) {
            System.out.println("Error al servidor");
        }
    }
}