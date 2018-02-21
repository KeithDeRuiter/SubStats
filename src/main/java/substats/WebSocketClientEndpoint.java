package substats;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 *
 * @author Keith
 */
@ClientEndpoint
public class WebSocketClientEndpoint {
    
    private final static Logger LOGGER = Logger.getLogger(WebSocketClientEndpoint.class.getName());
    
    Session session;
    
    public WebSocketClientEndpoint(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (DeploymentException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error connecting WebSocket to server", ex);
        }
    }
 
    /**
     * Callback for Connection opened events.
     * @param userSession The Session that was opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        session = userSession;
        LOGGER.info("Opened session: " + session.toString());
    }
 
    /**
     * Callback for Connection closed events.
     * @param userSession The Session being closed.
     * @param reason The reason the connection was closed.
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        LOGGER.info("Opened session: " + userSession.toString());
        session = null;
    }
 
    /**
     * Callback for message events.
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        LOGGER.info("WebSocket received message: " + message);
    }
 
    /**
     * Send a message using Async Remote.
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }
}
