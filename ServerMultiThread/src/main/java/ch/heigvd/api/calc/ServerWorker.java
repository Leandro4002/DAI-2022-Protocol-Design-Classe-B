package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {
    private Socket clientSocket;

    public static double runCalculation(String calculationToDo) {
        /*char[] legalCharacters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+'};
        for (int i = 0; i < calculationToDo.length(); ++i) {
            for (calculationToDo.charAt(i)) {
                input = input.replace('a','1');
                input = input.replace('b','2');
            }
        }

        calculationToDo.replace(" ", "");
        calculationToDo.replace(" ", "");
*/
        return 0;
    }

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());

    /**
     * Instantiation of a new worker mapped to a socket
     *
     * @param clientSocket connected to worker
     */
    public ServerWorker(Socket clientSocket) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        /* TODO: prepare everything for the ServerWorker to run when the
         *   server calls the ServerWorker.run method.
         *   Don't call the ServerWorker.run method here. It has to be called from the Server.
         */
        this.clientSocket = clientSocket;
    }

    /**
     * Run method of the thread.
     */
    @Override
    public void run() {
        LOG.info("Started new worker for client ip" + clientSocket.getInetAddress());
        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            String line;
            boolean isConnexionOver = false;
            JSONObject jo;

            while (!isConnexionOver) {
                if((line = in.readLine()) != null) {
                    LOG.info("Reading message \"" + line + "\"");
                    JSONParser parser = new JSONParser();

                    try{
                        JSONObject jsonObj = (JSONObject)parser.parse(line);

                        String messageType = (String)jsonObj.get("Type de message.");
                        switch(messageType) {
                            case "Bonjour.":
                                jo = new JSONObject();
                                jo.put("Type de message.", "Comment ça va ?");
                                JSONArray ja = new JSONArray();
                                ja.add("+");
                                jo.put("Opérandes supportées.", ja);
                                out.println(jo.toJSONString());
                                out.flush();
                                break;
                            case "Au revoir.":
                                isConnexionOver = true;
                                break;
                            case "Calcul, s'il te plaît.":
                                String calculationToDo = (String)jsonObj.get("Calcul à faire.");
                                break;
                            default:
                                jo = new JSONObject();
                                jo.put("Type de message.", "J'ai un problème.");
                                jo.put("Explication de mon problème.", "Je ne connais pas le type de message \"" + messageType + "\"");
                                out.println(jo.toJSONString());
                                out.flush();
                        }

                    } catch(ParseException pe) {
                        jo = new JSONObject();
                        jo.put("Type de message.", "J'ai un problème.");
                        jo.put("Explication de mon problème.", "Vous avez incorrectement formaté votre message JSON. J'exige vos excuses.");
                        out.println(jo.toJSONString());
                        out.flush();
                        LOG.warning("PARSE EXCEPTION : position=" + pe.getPosition());
                    }
                }
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception exception) {
            System.out.println("ERROR : " + exception.getMessage());
        }
    }
}