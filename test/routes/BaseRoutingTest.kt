package routes

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.mindeurfou.model.game.outgoing.Game
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.testing.*
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.ktor.ext.Koin
import java.lang.reflect.Type
import java.time.LocalDate

abstract class BaseRoutingTest {

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, object : JsonDeserializer<LocalDate> {
            override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
                return LocalDate.ofEpochDay(json!!.asString!!.toLong())
            }

    }).create()

    protected var koinModules: Module? = null
    protected var moduleList: Application.() -> Unit = {}

    init {
        stopKoin()
    }

    fun <R> withBaseTestApplication(test: TestApplicationEngine.() -> R) {
        withTestApplication({
            install(ContentNegotiation) { json() }
            koinModules?.let {
                install(Koin) {
                    modules(it)
                }
            }
            moduleList()
        }) { test() }
    }

    fun toJsonBody(obj: Any): String = gson.toJson(obj)

    fun <R> TestApplicationResponse.parseBody(clazz: Class<R>): R {
        return gson.fromJson(content, clazz)
    }

    fun <R> TestApplicationResponse.parseBodyList(clazz: Class<R>): List<R> {
        val itemType = object : TypeToken<List<R>>() {}.type
        return gson.fromJson(content, itemType)
    }

    internal inline fun <reified T> Gson.parseBodyy(json: String) =
        fromJson<T>(json, object : TypeToken<T>() {}.type)

}