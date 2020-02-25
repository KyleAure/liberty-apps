package io.websphereapps.jdbc;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;

@Path("resource")
@ApplicationScoped
@Produces(MediaType.TEXT_HTML)
public class TestResource {

    private static boolean dbCreated = false;

    @Resource
    private static DataSource ds;

    @PostConstruct
    public void initDataSource() throws NamingException, SQLException {
        //Create database table if not already created
        createIfNot();
    }

    private void createIfNot() throws SQLException {
        if (dbCreated == false) {
            //Get connection
            Connection con = ds.getConnection();
            try {
                //Create statement
                Statement stmt = con.createStatement();
                try {
                    //Drop table
                    stmt.execute("DROP TABLE MYTABLE");
                } catch (SQLException x) {
                    // probably didn't exist
                }
                //Create Table
                stmt.execute("CREATE TABLE MYTABLE (ID NUMBER NOT NULL PRIMARY KEY, STRVAL NVARCHAR2(40))");
                stmt.close();
            } finally {
                con.close();
            }
            //Mark created
            dbCreated = true;
        }
    }

    @GET
    @Path("/insert")
    public Response insert(
            @QueryParam("key") int key,
            @QueryParam("value") String value) throws SQLException {

        //Call createIfNot just in case
        createIfNot();

        //Create response message
        String msg = "<h1>Insert Method:</h1>";
        msg += System.lineSeparator() + "<p>Getting datababase connection</p>";
        Connection con = ds.getConnection();
        msg += System.lineSeparator() + "<p>Connection sucessful. Insert into database VALUES(" + key + ", " + value + ")</p>";

        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO MYTABLE VALUES(?,?)");
            ps.setInt(1, key);
            ps.setString(2, value);
            ps.executeUpdate();
            ps.close();
        } finally {
            con.close();
        }

        msg += System.lineSeparator() + "<p>Connection closed</p>";
        return Response.status(200).entity(msg).build();
    }

    @GET
    @Path("{key}")
    public Response getValue(@PathParam("key") int key) throws SQLException {
        //Call createIfNot just in case
        createIfNot();

        String msg = "<h1>GetValue Method:</h1>";

        try (Connection con = ds.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM MYTABLE WHERE ID = ?");
            try {
                ps.setInt(1, key);
                ResultSet results = ps.executeQuery();

                while (results.next()) {
                    msg += System.lineSeparator() + "<p>" + key + " " + results.getString(2) + "</p>";
                }

                results.close();
            } finally {
                ps.close();
            }
        }
        return Response.status(200).entity(msg).build();
    }

    @GET
    public Response getValues() throws SQLException {
        //Call createIfNot just in case
        createIfNot();

        String msg = "<h1>GetValues Method:</h1>";

        try (Connection con = ds.getConnection()) {
            Statement stmt = con.createStatement();
            try {
                ResultSet results = stmt.executeQuery("SELECT * FROM MYTABLE");

                while (results.next()) {
                    msg += System.lineSeparator() + "<p>" + results.getInt(1) + " " + results.getString(2) + "</p>";
                }

                results.close();
            } finally {
                stmt.close();
            }
        }

        return Response.status(200).entity(msg).build();
    }
}
