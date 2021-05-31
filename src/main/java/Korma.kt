import configs.app.AppConfiguration
import context.Context
import controllers.ControllerFactory
import data.DataContextFactory
import io.javalin.Javalin
import services.ServiceFactory
import providers.JWTProviderFactory

object Korma {

    @JvmStatic
    fun main(args: Array<String>) { // TODO: PORT and C1 -> config env

        // All context function on Roles

        // context 1

        val serviceFactory = ServiceFactory()
        val dataContextFactory = DataContextFactory()
        val jwtServiceProvider = JWTProviderFactory()

        Context(
                "c1",
                Javalin.create { config ->
                    run {
                        config.enableCorsForAllOrigins()
                        AppConfiguration(config, serviceFactory, dataContextFactory, jwtServiceProvider).getRolesConfig()
                    }
                }.start(7000),
                ControllerFactory(),
                dataContextFactory,
                serviceFactory,
                jwtServiceProvider
        )

        // context 2

        val serviceFactory2 = ServiceFactory()
        val dataContextFactory2 = DataContextFactory()
        val jwtServiceProvider2 = JWTProviderFactory()

        Context(
                "c2",
                Javalin.create{config ->
                    run {
                        config.enableCorsForAllOrigins()
                        AppConfiguration(config, serviceFactory2, dataContextFactory2, jwtServiceProvider2).getRolesConfig()
                    }
                }.start(7001),
                ControllerFactory(),
                dataContextFactory2,
                serviceFactory2,
                jwtServiceProvider2
        )
    }
}