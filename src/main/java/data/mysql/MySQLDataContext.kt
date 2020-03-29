package data.mysql

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class MySQLDataContext(DataBaseName: String, //  Database credentials

    var user: String, var password: String) {
    var dburlpath: String

    private val CONN: Connection? = null

    fun open() {
        val stmt: Statement? = null
        try {
            println("Connecting to database...")
            connection = DriverManager.getConnection(dburlpath, user, password)
            println("Connection created.")
        } catch (se: SQLException) { //Handle errors for JDBC
            se.printStackTrace()
        }
    }

    fun close(c: Connection) {
        try {
            c.close()
        } catch (se: SQLException) { //Handle errors for JDBC
            se.printStackTrace()
        } finally {
            try {
                if (connection != null) connection!!.close()
            } catch (se: SQLException) {
                se.printStackTrace()
            } //end finally try
        } //end try
        println("Data Goodbye!")
    }

    fun getConnection(): Connection? {
        return this.CONN
    }

    companion object {
        const val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
        const val DB_URL = "jdbc:mysql://localhost:3306/"
        const val TIMEZONE = "UTC"
        var connection: Connection? = null
            private set
    }

    init {
        dburlpath = "$DB_URL$DataBaseName?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=$TIMEZONE"
        connection = null
    }
}