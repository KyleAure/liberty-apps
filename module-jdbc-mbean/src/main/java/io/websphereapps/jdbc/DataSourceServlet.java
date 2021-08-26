package io.websphereapps.jdbc;

import javax.management.ObjectInstance;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class DataSourceServlet extends TestServlet {
	
	private static void respond(HttpServletResponse response, String... messages) throws IOException {
        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        for(String message: messages) {
        	out.println(message);
        	System.out.println(messages);
        }    	
	}
	
    public void getConnection( HttpServletRequest request, HttpServletResponse response ) throws Exception {
    	respond(response, getConnectionProxy());
    }


    public void getConnections( HttpServletRequest request, HttpServletResponse response ) throws Exception {
    	ArrayList<String> messages = new ArrayList<>();
    	ArrayList<Connection> connections = new ArrayList();
    	
    	// Get request param
    	int size = Integer.parseInt(request.getParameter(CONNECTIONS));
        
    	for(int i = 0; i < size; i++) {
    		try {
        		Connection conn = getConnectionSimple();
        		messages.add("Get connection call (" + i + "): " + conn);
        		connections.add(conn);
    		} catch (SQLException e) {
    			messages.add(e.toString());
    		}
    	}
    	
    	for(Connection conn : connections) {
    		messages.add("Close connection call: " + conn);
    		conn.close();
    	}
    	
    	String[] messagesArr = new String[messages.size()];
    	messagesArr = messages.toArray(messagesArr);
    	
    	respond(response, messagesArr);
    }
    
    public void showPoolContents( HttpServletRequest request, HttpServletResponse response ) throws Exception {    	
    	String message = "";
    	
        ObjectInstance bean= getMBeanObjectInstance();
		message += "KJA1017 POOL CONTENTS: " + getPoolContents(bean);      
        
		respond(response, message);
    }
    
    public void purge( HttpServletRequest request, HttpServletResponse response ) throws Exception {    	
    	String message = "";
    	
        ObjectInstance bean= getMBeanObjectInstance();
		mbs.invoke(bean.getObjectName(), "purgePoolContents", null, null);
		message += "KJA1017 POOL CONTENTS AFTER PURGE: " + getPoolContents(bean);      
        
		respond(response, message);
    }
	
    public void purgeAbort( HttpServletRequest request, HttpServletResponse response ) throws Exception {        
    	String message = "";
    	
        ObjectInstance bean = getMBeanObjectInstance();
		mbs.invoke(bean.getObjectName(), "purgePoolContents", new Object[] { "abort" }, null);
		message += "KJA1017 POOL CONTENTS AFTER PURGE ABORT: " + getPoolContents(bean);

		respond(response, message);
    }
    
    public void purgeImmediate( HttpServletRequest request, HttpServletResponse response ) throws Exception {        
    	String message = "";
    	
        ObjectInstance bean = getMBeanObjectInstance();
		mbs.invoke(bean.getObjectName(), "purgePoolContents", new Object[] { "immediate" }, null);
		message += "KJA1017 POOL CONTENTS AFTER PURGE IMMEDIATE: " + getPoolContents(bean);

		respond(response, message);
    }
    
    public void poolSize( HttpServletRequest request, HttpServletResponse response ) throws Exception {
    	String message = "";
    	
        ObjectInstance bean = getMBeanObjectInstance();
		message += "KJA1017 POOL SIZE: " + getPoolSize(bean);
		
		respond(response, message);
    }
}
