package services

import datamodels.Origin
import datamodels.Role
import kotliquery.Row
import kotliquery.Session
import kotliquery.queryOf
import data.DataContextFactory as DataContextFactoryOnSqLite

class AuthService (data: DataContextFactoryOnSqLite) {

    private var sessionContext: Session? = null

    private val convertRole: (Row) -> Role = { row ->
        Role(
                row.int("id"),
                row.int("user"),
                row.int("role")
        )
    }

    private val convertOrigin: (Row) -> Origin = { row ->
        Origin(
                row.int("id"),
                row.stringOrNull("key"),
                row.int("role")
        )
    }


    init {
        this.sessionContext = data.getKotlinQueryContextForSqlite("jdbc:sqlite:example.db")
    }

    fun getAppRoleByKey(key: String): List<Origin> {
        return this.sessionContext!!.run(
                queryOf("""select * from origins where key = :key""", mapOf("key" to key))
                        .map(convertOrigin).asList
        )
    }

    fun getUserRoleById(id: Int): List<Role> {
        return this.sessionContext!!.run(
                queryOf("""select * from roles where id = :id""", mapOf("id" to id))
                        .map(convertRole).asList
        )
    }

    fun getAllRoles(): List<Role> {
        return this.sessionContext!!.run(
                queryOf("select * from roles").map(convertRole).asList
        )
    }


}