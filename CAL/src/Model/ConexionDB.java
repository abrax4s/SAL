
package Model;

/**
 *
 * @author Adán
 */
import java.sql.*;
public class ConexionDB {
    protected static String usrDB = "AdminCAL_DB";
    protected static String PassDB ="C41LDBrl$";
    public static Connection con =null;
    public void abrirCon(){
        
        try{  
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=CAL_DB;user="+usrDB+";password="+PassDB+";";
        con= DriverManager.getConnection(connectionUrl);
        } catch (SQLException e){
            System.out.println("SQL Exception: "+ e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: "+ cE.toString());
        }
    }
    
    public Connection conectar()
    {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=CAL_DB;user="+usrDB+";password="+PassDB+";";
            con= DriverManager.getConnection(connectionUrl);
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }
        return con;
    }

    public void CerrarCon(){
        
    }
}
