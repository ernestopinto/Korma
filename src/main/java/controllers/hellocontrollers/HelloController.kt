package controllers.hellocontrollers

import services.ServiceFactory
import data.DataContextFactory
import io.javalin.http.Context
import kotliquery.queryOf
import kotliquery.sessionOf
import models.responses.Response

class HelloController(private val ctx: Context, private val dataContextFactory: DataContextFactory, serviceFactory: ServiceFactory) {

    private val dataService: ServiceFactory = serviceFactory
    private var response: Response? = null

    fun helloMessage(): Context {
        return ctx.result("Korma up and running!...")
    }

    fun testConnectionNewAge(): Context{
        val session = sessionOf("jdbc:sqlite:example.db", "", "")
        val allIdsQuery = queryOf("select * from users")
                .map { row -> row.int("id") }.asList
        val ids: List<Int> = session.run(allIdsQuery)
        return ctx.json(ids)
    }

    fun getAllIds (): Context {
        return ctx.json(dataService.getSqliteService(dataContextFactory).getAllIds())
    }

    fun getAllUsers (): Context {
        return ctx.json(Response(1, dataService.getSqliteService(dataContextFactory).getAllUsers()))
    }

    fun getUserById (): Context {
        return ctx.json(Response(1, dataService.getSqliteService(dataContextFactory).getUserById(ctx.pathParam("id"))))
    }

    fun getUserByIdMultiple (): Context {
        return ctx.json(Response(1, dataService.getSqliteService(dataContextFactory).getUserCredentialInterval(ctx.pathParam("c1"), ctx.pathParam("c2"))))
    }
}