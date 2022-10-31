package ch.heigvd.api.calc;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.Socket;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

/**
 * Calculator client implementation
 */
public class Client {

    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    /**
     * Main function to run client
     *
     * @param args no args required
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        Socket clientSocket;
        BufferedReader in = null;
        BufferedWriter out = null;
        JSONObject jojo = new JSONObject();
        JSONParser jp = new JSONParser();

        try {
            clientSocket = new Socket("192.168.43.107", 4242);
            System.out.println("Connection réussie");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return;
        }

        while (true) {
            try {
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                //String line;
                jojo.put("Type de message.", "Bonjour.");
                out.write(jojo.toJSONString());
                out.write("bonjoujour");
                System.out.println("Bonsoir");
                out.flush();
                while ((jojo = (JSONObject) jp.parse(in.readLine())) != null) {
                    String messageType = (String) jojo.get("Type de message.");
                    switch (messageType) {
                        case "Comment ça va?":
                            break;
                        case "J'ai un problème.":
                            break;

                    }
                    jojo.put("Type de message.", "Calcul s'il te plaît.");
                    jojo.put("Calcul à faire", "3+4");

                }

                clientSocket.close();
                in.close();
                out.close();

            } catch (Exception e) {
                System.out.println("OMG" + e.getMessage());
            }



            /* TODO: Implement the client here, according to your specification
             *   The client has to do the following:
             *   - connect to the server
             *   - initialize the dialog with the server according to your specification
             *   - In a loop:
             *     - read the command from the user on in (already created)
             *     - send the command to the server
             *     - read the response line from the server (using BufferedReader.readLine)
             */


        }
    }
}
