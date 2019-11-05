package info.mta.bustime;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.ShutdownHandler;

import java.io.IOException;
import java.net.URI;

public class StopViewServer {
    private static URI fileServerUri;
    private static Server server;

    public String getUrl() {
        return fileServerUri.toString();
    }

    public StopViewServer() {
        String projectRootDirectory = System.getProperty("user.dir");
//        Workaround that allows to get jetty run in the same folder no matter if the execution
//      was started from root or tests module
        if (projectRootDirectory.endsWith("ladders_ui_tests")) {
            projectRootDirectory = projectRootDirectory + "/../";
        }
        new StopViewServer(0, projectRootDirectory + "/files_server/src/main/resources");
    }

    private StopViewServer(int port, String path) {
        try {
            start(port, path);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void start(int port, String path) throws Exception {
        // Create a basic Jetty server object that will listen on port 8080.  Note that if you set this to port 0
        // then a randomly available port will be assigned that you can either look in the logs for the port,
        // or programmatically obtain it for use in test cases.
        server = new Server(port);
        // Create the ResourceHandler. It is the object that will actually handle the request for a given file. It is
        // a Jetty Handler object so it is suitable for chaining with other handlers as you will see in other examples.
        ResourceHandler resourceHandler = new ResourceHandler();
        // Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
        // In this example it is the current directory but it can be configured to anything that the jvm has access to.
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase(path);
        // Create the ShutdownHandler. It is the object that will actually handle the shutdown of server.
        // https://www.eclipse.org/jetty/documentation/9.4.x/shutdown-handler.html
        ShutdownHandler shutdownHandler = new ShutdownHandler("secret_password");
        // Add the ResourceHandler to the server.
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, shutdownHandler, new DefaultHandler()});
        server.setHandler(handlers);
        server.start();
        fileServerUri = server.getURI();
        System.out.println(String.format("Server started at '%s'", getUrl()));
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception ignored) {
        }
    }
}
