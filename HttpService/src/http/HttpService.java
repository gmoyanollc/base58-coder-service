/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;

/**
 *
 * @author g
 */
public class HttpService {
    private static final int DEFAULT_PORT = 8080;
    
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java HttpService service-name-1 [service-name-2 ...]");
            return;
        }
        new HttpService().init(args);
        //new HttpService().init("Base58HttpHandler.Base58HttpHandler");
    }
    
    public void init(String[] serviceName) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
              System.out.println("starting server...");
              System.out.println("  local host name: " + inetAddress.getHostName());
              System.out.println("  local host address: " + inetAddress.getHostAddress());
              System.out.println("  http port: " + DEFAULT_PORT);
              System.out.println("\nservices:");
              
            HttpServer server = HttpServer.create(new InetSocketAddress(DEFAULT_PORT), 0);
            server.setExecutor(null); // creates a default executor
            
            for (String serviceName1 : serviceName) {
                Class serviceClass = Class.forName(serviceName1);
                server.createContext('/' + serviceName1, (HttpHandler) serviceClass.newInstance());
                System.out.println("  'http://' ['localhost', fully-qualified-domain-name] ':" + DEFAULT_PORT + '/' + serviceName1);
            } 

            server.start();
        }
        catch(IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) { throw new RuntimeException(e); }
    }
    
    public static void printRequestHeaders(Headers requestHeaders) {
        try {
            Set<String> keySet = requestHeaders.keySet();

            for (String key : keySet) {
                List values = requestHeaders.get(key);
                String header = key + " = " + values.toString() + "\n";
                System.out.print(header);
            }
            
        } catch (Exception e) { throw new RuntimeException(e); }
    }
    
    public static String getRequestBody(InputStream inputStream) {
        String requestBodyString = null;
        if (inputStream != null) {
            try {
                int available = inputStream.available();
                if (available > 0) {
                    try (BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(inputStream));) {
                        String inputLine;
                        StringBuilder stringBuilder = new StringBuilder();

                        while ((inputLine = bufferedReader.readLine()) != null) {
                            stringBuilder.append(inputLine);
                            stringBuilder.append(System.lineSeparator());
                        }

                        bufferedReader.close();
                        requestBodyString = stringBuilder.toString();
                        System.out.println(requestBodyString);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Request body is empty");
                }
            } catch (Exception e) { throw new RuntimeException(e); }
        }
        return requestBodyString;
    }
    
    public static void printResponseHeaders(Headers responseHeaders) {
        try {
                Set<String> responseHeadersKeySet = responseHeaders.keySet();
                responseHeadersKeySet
                    .stream()
                    .map((key) -> {
                        List values = responseHeaders.get(key);
                        String header = key + " = " + 
                            values.toString() + "\n";
                        return header;
                    })
                    .forEach((header) -> {
                        System.out.print(header);
                    });
            
        } catch (Exception e) { throw new RuntimeException(e); }
    }
    
}
