package natour.issam.proyecto.es.proyecto_qiz;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import java.sql.*;

public class Conexion extends AsyncTask<Connection, Integer, Connection> {

    private final static String DB = "buhopl5_Quiz";
    private final static String RUTA = "buhoplace.com:3306/";
    private final static String URL = "jdbc:mysql://" + RUTA + DB;
    private final static String USER = "buhopl5_admin";
    private final static String PASSWORD = "admin_1993";
    private final static String DRIVER = "com.mysql.jdbc.Driver";

    Connection conn = null;
    int progress_status;
    ProgressBar bar;
    public Conexion() {

    }

    @Override
    public Connection doInBackground(Connection... strings) {

        try {
            Class.forName(DRIVER);

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn != null) {
                Log.i("Conexion", "Conexion correcta");

            } else {
                Log.i("Conexion", "Conexion incorrecta");
            }

        } catch (SQLException e) {
            Log.i("Conexion", e.toString());
        } catch (ClassNotFoundException e) {
            Log.i("Conexion", e.toString());
        }
        return conn;


    }
    public void setProgressBar(ProgressBar bar) {
        this.bar = bar;
    }

    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (this.bar != null) {
            bar.setProgress(values[0]);
        }
    }
    @Override
    protected void onPostExecute(Connection connection) {
        super.onPostExecute(connection);

    }
}

