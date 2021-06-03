package io.websphereapps.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {
	public static final String CONNECTIONS = "connections";
	public static final String SUCCESS = "SUCCESS";
	public static final String TEST_METHOD = "testMethod";
	
	protected static final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	protected static final String MBEAN_TYPE = "com.ibm.ws.jca.cm.mbean.ConnectionManagerMBean";
	protected static final String JNDI_NAME = "jdbc/testDS";
	
	@Resource(name = "jdbc/testDS")
	DataSource ds;
	
	protected String getConnectionProxy() throws InterruptedException {
		String result = "";
		
		int min = 1000; //1 second
		int max = 3000; //3 seconds
		long random_int = (int) Math.floor(Math.random()*(max-min+1)+min);
		
		
		try (Connection con = ds.getConnection()) {
			Thread.sleep(random_int);
			result += "KJA1017 get connection successful!";
		} catch (SQLException sql) {
			result += "KJA1017 get connection failed: " + sql.getMessage();
			sql.printStackTrace(System.out);
		}
		return result;
	}
	
	protected String getPoolContents(ObjectInstance bean) throws Exception {
        String contents = "   ";
        contents += (String) ManagementFactory.getPlatformMBeanServer().invoke(bean.getObjectName(), "showPoolContents", null, null);
        return contents.replace("\n", "\n   ");
    }
    
	protected int getPoolSize(ObjectInstance bean) throws Exception {
        return Integer.parseInt((String) ManagementFactory.getPlatformMBeanServer().getAttribute(bean.getObjectName(), "size"));
    }
    
	protected ObjectInstance getMBeanObjectInstance() throws Exception {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName obn = new ObjectName("WebSphere:type=" + MBEAN_TYPE + ",jndiName=" + JNDI_NAME + ",*");
        Set<ObjectInstance> s = mbs.queryMBeans(obn, null);
        if (s.size() != 1) {
            System.out.println("ERROR: Found incorrect number of MBeans (" + s.size() + ")");
            for (ObjectInstance i : s)
                System.out.println("  Found MBean: " + i.getObjectName());
            throw new Exception("Expected to find exactly 1 MBean, instead found " + s.size());
        }
        return s.iterator().next();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter(TEST_METHOD);

        System.out.println(">>> BEGIN: " + method);
        System.out.println("Request URL: " + request.getRequestURL() + '?' + request.getQueryString());
        PrintWriter writer = response.getWriter();
        if (method != null && method.length() > 0) {
            try {

                // Use reflection to try invoking various test method signatures:
                // 1)  method(HttpServletRequest request, HttpServletResponse response)
                // 2)  method()
                // 3)  use custom method invocation by calling invokeTest(method, request, response)
                try {
                    Method mthd = getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
                    mthd.invoke(this, request, response);
                } catch (NoSuchMethodException nsme) {
                    try {
                        Method mthd = getClass().getMethod(method, (Class<?>[]) null);
                        mthd.invoke(this);
                    } catch (NoSuchMethodException nsme1) {
                        invokeTest(method, request, response);
                    }
                }

                writer.println(SUCCESS);
            } catch (Throwable t) {
                if (t instanceof InvocationTargetException) {
                    t = t.getCause();
                }

                System.out.println("ERROR: " + t);
                StringWriter sw = new StringWriter();
                t.printStackTrace(new PrintWriter(sw));
                System.err.print(sw);

                writer.println("ERROR: Caught exception attempting to call test method " + method + " on servlet " + getClass().getName());
                t.printStackTrace(writer);
            }
        } else {
            System.out.println("ERROR: expected testMethod parameter");
            writer.println("ERROR: expected testMethod parameter");
        }

        writer.flush();
        writer.close();

        System.out.println("<<< END:   " + method);
    }
    
    /**
     * Implement this method for custom test invocation, such as specific test method signatures
     */
    protected void invokeTest(String method, HttpServletRequest request, HttpServletResponse response) throws Exception {
        throw new NoSuchMethodException("No such method '" + method + "' found on class "
                                        + getClass() + " with any of the following signatures:   "
                                        + method + "(HttpServletRequest, HttpServletResponse)   "
                                        + method + "()");
    }
}
