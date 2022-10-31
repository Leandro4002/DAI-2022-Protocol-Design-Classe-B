package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {
    private Socket clientSocket;

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
            String userInput = "";
            JSONObject jo = new JSONObject();

            boolean isConnexionOver = false;

            while (!isConnexionOver) {
                while((line = in.readLine()) != null) {
                    userInput += line;
                }

                if (!userInput.isEmpty()) {
                    JSONParser parser = new JSONParser();

                    try{
                        Object obj = parser.parse(userInput);
                        JSONObject jsonObj = (JSONObject)obj;

                        String messageType = (String)jsonObj.get("Type de message.");
                        switch(messageType) {
                            case "Bonjour.":
                                out.println("{\"Type de message\":\"\"}");
                                break;
                            case "Au revoir.":
                                isConnexionOver = true;
                                break;
                            default:
                                out.println("Je ne connais pas le type de message \"" + messageType +"\"");
                                out.flush();
                        }

                        LOG.info("Received a " + messageType);

                        LOG.info("Received user data \n" + jsonObj.toJSONString());
                    } catch(ParseException pe) {
                        System.out.println("PARSE EXCEPTION : position=" + pe.getPosition());
                    }

                    userInput = "";
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