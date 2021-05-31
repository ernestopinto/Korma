package context

import services.ServiceFactory
import controllers.ControllerFactory
import data.DataContextFactory
import io.javalin.Javalin
import io.javalin.core.JavalinConfig
import providers.JWTProviderFactory
import routing.RoutingAppMapper

class Context(
        contextKey: String,
        app: Javalin, controllerFactory: ControllerFactory,
        dataContextFactory: DataContextFactory,
        serviceFactory: ServiceFactory,
        jwtProvider: JWTProviderFactory
)
{

    private var App: Javalin? = null

    // on the context we inject the Data Context
    init{
        this.App = app
        // starts javalin & aplies routing
        RoutingAppMapper(contextKey, App!!, controllerFactory, dataContextFactory, serviceFactory, jwtProvider)
    }

}