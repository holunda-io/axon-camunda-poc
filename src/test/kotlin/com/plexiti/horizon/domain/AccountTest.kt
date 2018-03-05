package com.plexiti.horizon.domain

import com.plexiti.generics.UnitTest
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.Before
import org.junit.Test
import org.junit.experimental.categories.Category


/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
@Category(UnitTest::class)
class AccountTest {

    private lateinit var account: AggregateTestFixture<Account>

    @Before
    fun setUp() {
        account = AggregateTestFixture(Account::class.java)
        account.registerAnnotatedCommandHandler(AccountService(account.repository))
    }

    @Test
    fun testCreateAccount() {
        account.givenNoPriorActivity()
            .`when`(CreateAccount(AccountId("accountId"), "testAccount"))
            .expectEvents(AccountCreated(AccountId("accountId"), "testAccount"))
    }

    @Test
    fun testRenameAccount() {
        account.given(AccountCreated(AccountId("accountId"), "testAccount"))
            .`when`(RenameAccount(AccountId("accountId"), "testAccountRenamed"))
            .expectEvents(AccountRenamed(AccountId("accountId"), "testAccountRenamed"))
    }

    @Test
    fun testRenameRenamedAccount() {
        account.given(AccountCreated(AccountId("accountId"), "testAccount"), AccountRenamed(AccountId("accountId"), "testAccountRenamed"))
            .`when`(RenameAccount(AccountId("accountId"), "testAccount"))
            .expectNoEvents()
    }

}