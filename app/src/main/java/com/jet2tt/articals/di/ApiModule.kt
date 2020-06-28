package com.jet2tt.articals.di

import com.jet2tt.articals.BuildConfig
import com.jet2tt.articals.MyApplication
import com.jet2tt.articals.R
import com.jet2tt.articals.model.ArticalService
import com.jet2tt.articals.service.RetroCaller
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException


/**
 * Created by Anil 26/6/20
 */
@Module
class ApiModule {

    fun getUnsafeOkHttpClient():OkHttpClient{
        /**
         * for SSL pining and networkSecurityConfig in Manifest for above N android
         */
        val certificatePinner = CertificatePinner.Builder()
            .add(
                "https://5e99a9b1bc561b0016af3540.mockapi.io",
                "sha256/nWFvlx12M290ZBO2sHlnuo/Z8m/8z5yaSynkSG870M0=").build()

        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.getSocketFactory()
            val builder = OkHttpClient.Builder()

            // response in log can see in debug apk only not in release for security purpose
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(logging)
            }
            builder.sslSocketFactory(sslSocketFactory)
            builder.writeTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .certificatePinner(certificatePinner)
            builder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String?, session: SSLSession?): Boolean {
                    return true
                }
            })
          return  builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }


    @Provides
    fun provideJ2ttService(): RetroCaller{
//        val logging = HttpLoggingInterceptor()
//        logging.level = HttpLoggingInterceptor.Level.BODY
//        var httpClient = OkHttpClient.Builder().addInterceptor(logging)
//            .addInterceptor(object:Interceptor{
//                override fun intercept(chain: Interceptor.Chain): Response {
//                    val original: Request = chain.request()
//                    val request = original.newBuilder()
//                        .addHeader("Content-Type", "application/json")
//                        .method(original.method(), original.body())
//                        .build()
//                    return chain.proceed(request)
//                }
//            })
//
//            .writeTimeout(10, TimeUnit.SECONDS)
//            .readTimeout(10, TimeUnit.SECONDS)
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .certificatePinner(certificatePinner)

        return Retrofit.Builder()
            .baseUrl(MyApplication.getContext()!!.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            // callAdapterFactory will transform the reposnse into the type which we need in the API service
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build()
            .create(RetroCaller::class.java)


    }

    @Provides
    fun provideArticalsService(): ArticalService{
        return ArticalService()
    }


}