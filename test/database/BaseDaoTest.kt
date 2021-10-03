package database

import io.ktor.application.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.ktor.ext.Koin
import org.koin.core.module.Module
import org.koin.core.context.stopKoin

abstract class BaseDaoTest {

    protected var koinModules: Module? = null

    init {
        stopKoin()
    }

    fun <R> withBaseTestApplication(test: TestApplicationEngine.() -> R) {
        withTestApplication( {
            koinModules?.let {
                install(Koin) {
                    modules(it)
                }
            }
        }) { test() }
    }

    @BeforeEach
    open fun setup() {
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
    }

    @AfterEach
    open fun tearDown() {
        transaction {
            dropSchema()
        }
    }

    abstract fun createSchema()

    abstract fun dropSchema()
}