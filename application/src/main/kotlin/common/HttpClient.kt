package strangler

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class TrustAllX509TrustManager : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

    override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}

    override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
}

object SslSettings {

    fun getSslContext(): SSLContext? {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, listOf(TrustAllX509TrustManager()).toTypedArray(), null)
        return sslContext
    }

    fun getTrustManager(): X509TrustManager {
        return TrustAllX509TrustManager()
    }
}

/*
This is quite expensive, so we avoid instantiating it multiple
times and just once, and reuse the client.
 */
private val client = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true
        })
    }
    engine {
        config {
            followRedirects(true)
        }
        config {
            sslSocketFactory(SslSettings.getSslContext()!!.socketFactory, TrustAllX509TrustManager())
        }
    }
}

fun httpClient() = client
