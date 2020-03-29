package controllers.users

import data.DataContextFactory
import io.javalin.http.Context
import models.responses.Response
import services.ServiceFactory

class UsersController (private val ctx: Context, private val dataContextFactory: DataContextFactory, serviceFactory: ServiceFactory){

    private val dataService: ServiceFactory = serviceFactory
    private var response: Response? = null


    fun getAllUsers (): Context {
        return ctx.json(Response(1, dataService.getUsersService(dataContextFactory).getAllUsers()))
    }

    fun getUserById (): Context {
        return ctx.json(Response(1, dataService.getUsersService(dataContextFactory).getUserById(ctx.pathParam("id"))))
    }

    fun getUserByIdMultiple (): Context {
        return ctx.json(Response(1, dataService.getUsersService(dataContextFactory).getUserCredentialInterval(ctx.pathParam("c1"), ctx.pathParam("c2"))))
    }
}