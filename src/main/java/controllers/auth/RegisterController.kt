package controllers.auth

import com.fasterxml.jackson.core.JsonParser
import data.DataContextFactory
import datamodels.RegisterUser
import io.javalin.http.Context
import models.responses.Response
import services.ServiceFactory

class RegisterController (
        private val ctx: Context,
        private val dataContextFactory: DataContextFactory,
        serviceFactory: ServiceFactory
){

    private val dataService: ServiceFactory = serviceFactory
    private var incomingRegister: RegisterUser? = null

    init {

    }

    fun registerUser(): Context{

        val valid = ctx.bodyValidator<RegisterUser>()
        return ctx.json(Response(1, valid.get()))
    }
}

