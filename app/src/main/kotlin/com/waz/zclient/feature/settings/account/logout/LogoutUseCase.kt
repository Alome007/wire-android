package com.waz.zclient.feature.settings.account.logout

import com.waz.zclient.core.exception.Failure
import com.waz.zclient.core.functional.Either
import com.waz.zclient.core.network.accesstoken.AccessTokenRepository
import com.waz.zclient.core.usecase.UseCase
import com.waz.zclient.shared.accounts.AccountsRepository
import com.waz.zclient.shared.accounts.ActiveAccount
import com.waz.zclient.storage.pref.global.GlobalPreferences
import kotlinx.coroutines.runBlocking

class LogoutUseCase(
    private val globalPreferences: GlobalPreferences,
    private val accountsRepository: AccountsRepository,
    private val accessTokenRepository: AccessTokenRepository
) : UseCase<Unit, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, Unit> = with(accessTokenRepository) {
        accountsRepository.logout(refreshToken().token, accessToken().token)
    }

    private fun updateActiveAccounts(activeAccounts: List<ActiveAccount>) = runBlocking {
        val currentAccount = activeAccounts.first { it.id == globalPreferences.activeUserId }
        activeAccounts.firstOrNull { it.id != globalPreferences.activeUserId }?.let {
            globalPreferences.activeUserId = it.id
        }
        accountsRepository.deleteAccountFromDevice(currentAccount)
    }
}
