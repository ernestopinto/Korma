package routing

import configs.app.AppConfiguration
import controllers.ControllerFactory
import services.ServiceFactory
import data.DataContextFactory
import io.javalin.Javalin
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.http.Context
import models.responses.Response
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths

class RoutingAppMapper(
        AppContextCode: String?,
        App: Javalin,
        Controllers: ControllerFactory?,
        dataContextFactory: DataContextFactory?,
        serviceFactory: ServiceFactory?
) {

    init { /// put here your routing for your given AppContextCode

        when (AppContextCode) {
            "c1" -> {
                deployRoutesForContextKorma(App, Controllers, dataContextFactory, serviceFactory)
            }
            "c2" -> {
                deployRoutesForContextJalfrezi(App)
            }
            else -> { // default context
                // define your default context
                // deploys Korma by default
                deployRoutesForContextKorma(App, Controllers, dataContextFactory, serviceFactory)
            }
        }
    }

    private fun deployRoutesForContextKorma(
            app: Javalin,
            Controllers: ControllerFactory?,
            dataContextFactory: DataContextFactory?,
            serviceFactory: ServiceFactory?
    ) {

        // App Korma

        // inbound

        app.before { ctx ->
            run {
                println("... arrived request -> controlled options")
                println("v-key -> " + ctx.header("v-key"))
            }
        }

        //

        // WEB context

        app.get("/web", { ctx ->
            ctx.json(Response(1, "Korma Web Framework up and running..."))
        }, roles(AppConfiguration.MyRole.WEB_CONTEXT))


        app.get("web/hello", { ctx ->
            Controllers!!.getHelloController(ctx, dataContextFactory, serviceFactory).helloMessage()
        }, roles(AppConfiguration.MyRole.WEB_CONTEXT))


        // ADMIN context

        // Users

        app.get("users", { ctx ->
            Controllers!!.getUsersController(ctx, dataContextFactory, serviceFactory).getAllUsers()
        }, roles(AppConfiguration.MyRole.ADMIN))

        app.get("/users/:id", { ctx ->
            Controllers!!.getUsersController(ctx, dataContextFactory, serviceFactory).getUserById()
        }, roles(AppConfiguration.MyRole.ADMIN))

        //


        // API & WEB context

        app.get("/", { ctx ->
            ctx.json(Response(1, "Korma Web Framework up and running..."))
        }, roles(
                AppConfiguration.MyRole.ADMIN,
                AppConfiguration.MyRole.WEB_CONTEXT,
                AppConfiguration.MyRole.API)
        )

        /// FILE -> JPG Image - jpg

        app["resource/jpg/:name", { ctx: Context ->
            run {
                try {
                    val fileName = ctx.pathParam("name")
                    val path = "upload/$fileName.jpg"
                    val p = Paths.get(path)
                    val `is`: InputStream = FileInputStream(p.toFile())
                    ctx.res.contentType = Files.probeContentType(p)
                    ctx.res.setHeader("Content-Disposition", "inline; filename=" + p.fileName)
                    val buffer = ByteArray(1024)
                    var len: Int
                    while (`is`.read(buffer).also { len = it } > 0) {
                        ctx.res.outputStream.write(buffer, 0, len)
                    }
                    val result = IOUtils.toString(`is`)
                    `is`.close()
                    ctx.render(result.trim { it <= ' ' })
                } catch (e: Exception) { // handle the exceptions the way you want
                    ctx.status(500)
                }
            }
        }, roles(AppConfiguration.MyRole.ADMIN, AppConfiguration.MyRole.USER)]

    }


    private fun deployRoutesForContextJalfrezi(
            app: Javalin
    ) {

        // App on context 2

        // inbound

        app.before { ctx ->
            run {
                println("... arrived request -> controlled options")
                println("v-code -> " + ctx.header("v-key"))
            }
        }

        app.get("/") { ctx ->
            ctx.result("Hello from Korma Context 2 - Jalfrezi")
        }

        ///
        app.post("/upload", { ctx: Context ->
            run {
                val upload = File("upload")
                if (!upload.exists() && !upload.mkdirs()) {
                    throw RuntimeException("Failed to create directory " + upload.absolutePath)
                }
                // apache commons-fileupload to handle file upload
                val factory = DiskFileItemFactory()
                factory.repository = upload
                val fileUpload = ServletFileUpload(factory)
                val items = fileUpload.parseRequest(ctx.req)
                // image is the field name that we want to save
                val item = items.stream()
                        .filter { e: FileItem -> "image" == e.fieldName }
                        .findFirst().get()
                val fileName = item.name
                item.write(File("upload", fileName))
                ctx.result("ok")
            }
        }, roles(AppConfiguration.MyRole.ADMIN))

    }
}