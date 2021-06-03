package it.websphereapps.jdbc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.websphereapps.jdbc.TestServlet;

public class TestServletClient {
    public static final String SUCCESS = TestServlet.SUCCESS;
    public static final String TEST_METHOD = TestServlet.TEST_METHOD;
    public static final String CONNECTIONS = TestServlet.CONNECTIONS;
    
    public static StringBuilder runTestWithResponse(String testMethod, String... queries) throws Exception {
        String port = System.getProperty("http.port");
        String context = System.getProperty("context.root");
        String rootUrl = "http://localhost:" + port + "/" + context + "?" + TEST_METHOD + "=" + testMethod;
        
        for (String query : queries) {
        	rootUrl += "&" + query;
        }
        
        URL url = new URL(rootUrl);
        System.out.println("KJA1017 URL is " + url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("GET");
            InputStream is = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String sep = System.getProperty("line.separator");
            StringBuilder lines = new StringBuilder();

            // Send output from servlet to console output
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                lines.append(line).append(sep);
                System.out.println("KJA1017 line is " + line);
            }

            return lines;
        } finally {
            con.disconnect();
        }
    }

}
