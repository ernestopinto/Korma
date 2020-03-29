package data

import kotliquery.Session
import data.mysql.MySQLDataContext
import kotliquery.sessionOf

class DataContextFactory
{
    fun getKotlinQueryContextForSqlite(path: String): Session? {
        return sessionOf(path, "", "")
    }

    fun getKotlinQueryContextForMySql(params: ArrayList<String>): Session? {
        return sessionOf(params[0], params[1], params[2])
    }
}