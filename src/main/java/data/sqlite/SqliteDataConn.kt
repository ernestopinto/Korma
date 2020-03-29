package data.sqlite

import com.mysql.cj.x.protobuf.MysqlxDatatypes
import datamodels.User
import java.sql.*

class SqliteDataConn(dataBaseName: String = "", pathDatabase: String?) {

    private var dataBaseName: String = ""
    private var conn: Connection? = null

    fun executeMySQLQuery(query: String = "select * from users", dataColumns: ArrayList<String>?): ResultSet? {
        var stmt: Statement? = null
        var resultset: ResultSet? = null
        try {
            stmt = this.conn!!.createStatement()
            resultset = stmt!!.executeQuery(query)
            if (stmt.execute(query)) {
                resultset = stmt.resultSet
            }
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } finally {
            // release resources
            if (resultset != null) {
                try {
                    resultset.close()
                } catch (sqlEx: SQLException) {
                }
                resultset = null
            }
            if (stmt != null) {
                try {
                    stmt.close()
                    println("stmt closed")
                } catch (sqlEx: SQLException) {
                    println(sqlEx)
                }
            }
            if (conn != null) {
                try {
                    conn!!.close()
                    println("conn closed")
                } catch (sqlEx: SQLException) {
                    println(sqlEx)
                }
                conn = null
            }
        }
        return resultset
    }

    private fun connect(name: String, path: String) {
        try { // db parameters
            val url = "jdbc:sqlite:$path$name.db"
            // create a connection to the database
            this.conn = DriverManager.getConnection(url)
            println("Connection to SQLite has been established.")
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
    }

    init {
        this.dataBaseName = dataBaseName
        if (pathDatabase != null) {
            this.connect(dataBaseName, pathDatabase)
        } else {
            this.connect(dataBaseName, "C:/sqlite/db")
        }
    }

}