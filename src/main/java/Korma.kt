import configs.app.AppConfiguration
import context.Context
import controllers.ControllerFactory
import data.DataContextFactory
import io.javalin.Javalin
import services.ServiceFactory

object Korma {

    @JvmStatic
    fun main(args: Array<String>) { // TODO: PORT and C1 -> config env

        // All context function on Roles

        // context 1

        val serviceFactory = ServiceFactory()
        val dataContextFactory = DataContextFactory()

        Context(
                "c1",
                Javalin.create { config ->
                    run {
                        config.enableCorsForAllOrigins()
                        AppConfiguration(config, serviceFactory, dataContextFactory).getRolesConfig()
                    }
                }.start(7000),
                ControllerFactory(),
                dataContextFactory,
                serviceFactory
        )

        // context 2

        Context(
                "c2",
                Javalin.create{config ->
                    run {
                        config.enableCorsForAllOrigins()
                        AppConfiguration(config, serviceFactory, dataContextFactory).getRolesConfig()
                    }
                }.start(7001),
                ControllerFactory(),
                dataContextFactory,
                serviceFactory
        )
    }
}