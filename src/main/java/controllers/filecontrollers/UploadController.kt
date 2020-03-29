package controllers.filecontrollers

import services.ServiceFactory
import data.DataContextFactory
import io.javalin.http.Context

class UploadController(
        private val ctx: Context, private val dataContextFactory: DataContextFactory, serviceFactory: ServiceFactory
) {

}