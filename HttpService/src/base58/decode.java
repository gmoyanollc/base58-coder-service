/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base58;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bitcoinj.core.Base58;

/**
 *
 * @author g
 */
public class decode implements HttpHandler {
    @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                System.out.println("request from remote address: " + httpExchange.getRemoteAddress());
                Headers requestHeaders = httpExchange.getRequestHeaders();
                http.HttpService.printRequestHeaders(requestHeaders);
                InputStream inputStream = httpExchange.getRequestBody();
                String requestBody = http.HttpService.getRequestBody(inputStream);
                
                String response = decodeBase58(requestBody);
                
                Headers responseHeaders = httpExchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/xml");
                http.HttpService.printResponseHeaders(responseHeaders);
                System.out.println(response);
                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
                System.out.println("response to remote address: " + httpExchange.getRemoteAddress());
            
            }
            catch(Exception e) { throw new RuntimeException(e); }
        }
    
        private String decodeBase58(String stringIn) {
        try {
            byte[] byteOut = Base58.decode(stringIn.replaceAll("\n", ""));
            return new String(byteOut);
        } catch(Exception e) { throw new RuntimeException(e); }
    }
}
