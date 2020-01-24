package com.waz.zclient.accounts

import com.waz.zclient.core.extension.empty
import com.waz.zclient.core.network.accesstoken.AccessToken
import com.waz.zclient.core.network.accesstoken.AccessTokenMapper
import com.waz.zclient.storage.db.accountdata.ActiveAccountsEntity
import com.waz.zclient.storage.db.accountdata.SsoIdEntity

data class ActiveAccounts(
    val id: String,
    val teamId: String?,
    val accessToken: AccessToken?,
    val refreshToken: String,
    val pushToken: String?,
    val ssoId: SsoId?)

data class SsoId(
    val subject: String,
    val tenant: String
) {

    companion object {
        val EMPTY = SsoId(String.empty(), String.empty())
    }
}

class AccountMapper {

    private val accessTokenMapper = AccessTokenMapper()

    fun toEntity(activeAccounts: ActiveAccounts) = ActiveAccountsEntity(
        id = activeAccounts.id,
        teamId = activeAccounts.teamId,
        accessToken = activeAccounts.accessToken?.let { accessTokenMapper.toEntity(it) },
        refreshToken = activeAccounts.refreshToken,
        pushToken = activeAccounts.pushToken,
        ssoId = activeAccounts.ssoId?.let { toEntity(it) })

    private fun toEntity(ssoId: SsoId) = SsoIdEntity(
        subject = ssoId.subject,
        tenant = ssoId.tenant
    )

    fun from(activeAccountEntity: ActiveAccountsEntity) = ActiveAccounts(
        id = activeAccountEntity.id,
        teamId = activeAccountEntity.teamId,
        accessToken = activeAccountEntity.accessToken?.let { accessTokenMapper.from(it) },
        refreshToken = activeAccountEntity.refreshToken,
        pushToken = activeAccountEntity.pushToken,
        ssoId = activeAccountEntity.ssoId?.let { from(it) }
    )

    private fun from(ssoId: SsoIdEntity) = SsoId(
        subject = ssoId.subject,
        tenant = ssoId.tenant
    )
}
