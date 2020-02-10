package com.waz.zclient.core.network.api.token

import com.waz.zclient.core.exception.Failure
import com.waz.zclient.core.functional.Either
import com.waz.zclient.core.network.ApiService
import com.waz.zclient.core.network.NetworkHandler

class TokenService(
    override val networkHandler: NetworkHandler,
    private val tokenApi: TokenApi
) : ApiService() {

    suspend fun renewAccessToken(refreshToken: String): Either<Failure, AccessTokenResponse> =
        request(AccessTokenResponse.EMPTY) {
            tokenApi.access(generateCookieHeader(refreshToken))
        }

    suspend fun logout(refreshToken: String): Either<Failure, Unit> =
        request { tokenApi.logout(generateCookieHeader(refreshToken)) }

    private fun generateCookieHeader(refreshToken: String) = mapOf("cookie" to "zuid=$refreshToken")
}
