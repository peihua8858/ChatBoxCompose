import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

expect fun HttpClient(/*block: HttpClientConfig<*>.() -> Unit*/): HttpClient