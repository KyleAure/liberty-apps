package personal.websphereapps.app.sysinfo;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("system")
public class SysinfoApp extends Application {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
