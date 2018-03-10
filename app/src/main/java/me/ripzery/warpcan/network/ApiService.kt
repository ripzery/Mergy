package me.ripzery.warpcan.network

import com.google.gson.GsonBuilder
import me.ripzery.warpcan.MergyApplication
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 3/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object ApiService {
//    private val BASE_URL = "https://isetan-admin.madusoft.co.th"
    private val PRODUCTION_BASE_URL = "https://warpcan-admin.isetanbkk.com"
    private val authorizationToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImNhMWIxYmVkYTJkZTBlNGFjNmExZDg3YmM2YzBmOGMzYzNkMTZlZDhkMTE5MGUyNWEzOGYyZGVmNzcxZWI0YWQ3MWNlY2VjYTQxNzVmZWVhIn0.eyJhdWQiOiIyIiwianRpIjoiY2ExYjFiZWRhMmRlMGU0YWM2YTFkODdiYzZjMGY4YzNjM2QxNmVkOGQxMTkwZTI1YTM4ZjJkZWY3NzFlYjRhZDcxY2VjZWNhNDE3NWZlZWEiLCJpYXQiOjE1MTcwNDk5MzksIm5iZiI6MTUxNzA0OTkzOSwiZXhwIjoxNjc0ODE2MzM5LCJzdWIiOiIxIiwic2NvcGVzIjpbIioiXX0.sbalqELpng6nEw-gytdjgs0HcKrsdKdadW_zbsPJvqxUhsToN1MoppRKdS0Mdg2ei-QmPedGHfugwxAz20W3X0YcRbIJD9we8AH1XN0LFoELgr-iM-RCwpn-cEXpOvwmDacDfftl8UcVfz0304PaAn8LZERVqOH68jPmCmkC17hKejpjFbyro9T1ta9juUaDOrCxd1dpLSIM-wFoa7eOYhO3liic9xYnLifZvyuMszISjz1OMeyr0lYW6EPKdd8H5XTIFQjfzhe_NR9Rxoo6ISzQOYH3DsREMR-cagEfioIuSWWQMAaGdumL7O4HEoYcQUJYwCV7KpjTQGTTZq4xbe78Xoi3MRZlkRDlFJKHF1OXI1Uj8kwl_GbcLuTykdrRHO5hpEnQjE-hhmk5muWy9gO--5bV8v8Y06TofKqSWPKg3c-zyTBhf5Ci0--TiVUrFr0gnflcH5fNZF9nrGWxoeGWYWtuh4eA9jZ4KUCqz2oFMInJqoIJ98wemKrYtZwYlPmUt5h2YlDYL40cMRIb0nbI47zGoAMIBTlGxTZOzdk2XFEqMehbh5b52IirxjCVVkbvbNawsxJ3xMlHw3faTESNBd_2DPsa7whnWqZn-CUshZ3q3Ed_OKBQ_Zd2a_GT8TV-zoBSy0RncbmErY1GVRdRkqxdFwTOD25AmFHbj4E"
    val Isetan: IsetanEndpoints by lazy {
        create()
    }


    private fun create(): IsetanEndpoints {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
//                .addInterceptor {
//                    var originalResponse = it.proceed(it.request())
//                    val endpoint = it.request().url().pathSegments().last()
//                    if (endpoint == "index") {
//                        val chain = it
//                        try {
//                            return@addInterceptor chain.proceed(chain.request())
//                        } catch (e: Exception) {
//                            val offlineRequest = chain.request().newBuilder()
//                                    .header("Cache-Control", "public, only-if-cached," +
//                                            "max-stale=" + 60 * 60 * 24)
//                                    .build()
//                            return@addInterceptor chain.proceed(offlineRequest)
//                        }
//
//                    }
//                    originalResponse
//                }
                .addInterceptor {
                    val request = it.request().newBuilder().addHeader("Authorization", authorizationToken).build()
                    it.proceed(request)
                }
                .addInterceptor(interceptor)
                .cache(MergyApplication.cache)
                .build()

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

        val retrofit = Retrofit.Builder()
                .baseUrl(PRODUCTION_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()

        return retrofit.create(IsetanEndpoints::class.java)
    }
}