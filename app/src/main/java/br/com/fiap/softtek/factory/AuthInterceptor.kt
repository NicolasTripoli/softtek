import android.content.Context
import br.com.fiap.softtek.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlPath = request.url().encodedPath()
        if (urlPath.endsWith("/api/v1/auth/login")) {
            return chain.proceed(request)
        }

        val token = TokenManager.getToken(context)
        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}