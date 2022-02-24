/* Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.*/
/*
   DESCRIPTION    
   The code sample shows how to use the DataSource API to establish a connection
   to the Database. You can specify properties with "setConnectionProperties".
   This is the recommended way to create connections to the Database.

   Note that an instance of oracle.jdbc.pool.OracleDataSource doesn't provide
   any connection pooling. It's just a connection factory. A connection pool,
   such as Universal Connection Pool (UCP), can be configured to use an
   instance of oracle.jdbc.pool.OracleDataSource to create connections and 
   then cache them.
    
    Step 1: Enter the Database details in this file. 
            DB_USER, DB_PASSWORD and DB_URL are required
    Step 2: Run the sample with "ant DataSourceSample"
  
   NOTES
    Use JDK 1.7 and above

   MODIFIED    (MM/DD/YY)
    nbsundar    02/17/15 - Creation 
 */

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class DataSourceSample {
    final static String DB_URL = "jdbc:oracle:thin:@localhost:10006/BWDOMAINSRV001_461_ac_dbaas";

//    final static String DB_USER = "pdbadmin";
//    final static String DB_PASSWORD = "xxx";

    final static String DB_USER = "TIBTADM_OWNER";
    final static String DB_PASSWORD = "xxx";

    public static void main(String args[]) throws SQLException, InterruptedException {

        // These properties are passed in the Run Configuration (Add VM Options)
//        System.setProperty("oracle.net.encryption_client", "required");
//        System.setProperty("oracle.net.encryption_types_client", "AES256");

        Properties info = new Properties();
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
//        info.put(OracleConnection.CONNECTION_PROPERTY_THIN_NET_ENCRYPTION_LEVEL, "REQUIRED");
//        info.put(OracleConnection.CONNECTION_PROPERTY_THIN_NET_ENCRYPTION_TYPES, "AES256");


        // Print out Java System Properties
        List<String> properties = new ArrayList<>();
        System.getProperties().forEach((k, v) -> properties.add(k + " : " + v));
        properties.stream()
                .sorted()
                .collect(Collectors.toList())
                .forEach(s -> System.out.println(s));

        OracleDataSource ods = new OracleDataSource();
        ods.setURL(DB_URL);
        ods.setConnectionProperties(info);

        System.out.println();
        System.out.println(info);
        System.out.println();


        // With AutoCloseable, the connection is closed automatically.
        try (OracleConnection connection = (OracleConnection) ods.getConnection()) {
            // Get the JDBC driver name and version
            DatabaseMetaData dbmd = connection.getMetaData();
            System.out.println("Driver Name: " + dbmd.getDriverName());
            System.out.println("Driver Version: " + dbmd.getDriverVersion());
            // Print some connection properties
            System.out.println("Default Row Prefetch Value is: " + connection.getDefaultRowPrefetch());
            System.out.println("Database Username is: " + connection.getUserName());
            System.out.println();

            Thread.sleep(3_600_000);  // Sleep for 1 hour to keep the session open...

        }
    }

}
