package controllers

import controllers.auth.AuthController
import services.ServiceFactory
import controllers.hellocontrollers.HelloController
import controllers.filecontrollers.UploadController
import controllers.users.UsersController
import data.DataContextFactory
import io.javalin.http.Context
import providers.JWTProviderFactory

class ControllerFactory {

    // Inscribe All Controllers

    fun getHelloController(context: Context?,
                           dataContextFactory: DataContextFactory?,
                           serviceFactory: ServiceFactory?
    ): HelloController {
        return HelloController(context!!, dataContextFactory!!, serviceFactory!!)
    }

    fun getUsersController(context: Context?,
                           dataContextFactory: DataContextFactory?,
                           serviceFactory: ServiceFactory?
    ): UsersController {
        return UsersController(context!!, dataContextFactory!!, serviceFactory!!)
    }

    fun getAuthController (
            context: Context?,
            dataContextFactory: DataContextFactory?,
            serviceFactory: ServiceFactory?,
            jwtProvider: JWTProviderFactory
    ): AuthController {
        return AuthController(context!!, dataContextFactory!!, serviceFactory!!, jwtProvider)
    }
}