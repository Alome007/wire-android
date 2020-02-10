package com.waz.zclient.settings.account.logout

import com.waz.zclient.accounts.AccountsRepository
import com.waz.zclient.accounts.ActiveAccounts
import com.waz.zclient.core.exception.Failure
import com.waz.zclient.core.functional.Either
import com.waz.zclient.core.functional.flatMap
import com.waz.zclient.core.functional.onSuccess
import com.waz.zclient.core.network.accesstoken.AccessTokenRepository
import com.waz.zclient.core.usecase.UseCase
import com.waz.zclient.storage.pref.GlobalPreferences
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class LogoutUseCase(
    private val globalPreferences: GlobalPreferences,
    private val accountsRepository: AccountsRepository,
    private val accessTokenRepository: AccessTokenRepository
) : UseCase<Unit, Unit>() {

    override suspend fun run(params: Unit): Either<Failure, Unit> {
        return accountsRepository.activeAccounts()
            .flatMap {
                updateActiveAccounts(it)
            }
    }

    private fun updateActiveAccounts(activeAccounts: List<ActiveAccounts>) = runBlocking {
        val currentAccount = activeAccounts.first { it.id == globalPreferences.activeUserId }
        activeAccounts.firstOrNull { it.id != globalPreferences.activeUserId }?.let {
            globalPreferences.activeUserId = it.id
        }
        accountsRepository.removeAccount(currentAccount).onSuccess {
            Timber.e("local storage account removal success")
        }
    }
}
