package controllers

import services.ServiceFactory
import controllers.hellocontrollers.HelloController
import controllers.filecontrollers.UploadController
import controllers.users.UsersController
import data.DataContextFactory
import io.javalin.http.Context

class ControllerFactory {

    // Inscribe All Controllers

    fun getHelloController(context: Context?, dataContextFactory: DataContextFactory?, serviceFactory: ServiceFactory?): HelloController {
        return HelloController(context!!, dataContextFactory!!, serviceFactory!!)
    }

    fun getUploadController(context: Context?, dataContextFactory: DataContextFactory?, serviceFactory: ServiceFactory?): UploadController {
        return UploadController(context!!, dataContextFactory!!, serviceFactory!!)
    }

    fun getUsersController(context: Context?, dataContextFactory: DataContextFactory?, serviceFactory: ServiceFactory?): UsersController {
        return UsersController(context!!, dataContextFactory!!, serviceFactory!!)
    }
}