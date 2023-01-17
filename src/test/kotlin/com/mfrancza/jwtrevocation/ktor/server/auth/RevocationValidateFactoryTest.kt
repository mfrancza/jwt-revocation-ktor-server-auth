package com.mfrancza.jwtrevocation.ktor.server.auth

import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.Payload
import com.mfrancza.jwtrevocation.rules.Rule
import com.mfrancza.jwtrevocation.rules.RuleSet
import com.mfrancza.jwtrevocation.rules.conditions.StringEquals
import io.ktor.http.Parameters
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.Principal
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.response.ApplicationResponse
import io.ktor.util.Attributes
import kotlinx.coroutines.runBlocking
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals


class RevocationValidateFactoryTest {

    private val referenceEpochSeconds : Long = 1673123605

    @Test
    fun validateReturnsNullWhenMet() {
        val validate = RevocationValidateFactory {
            RuleSet(
                rules = listOf(
                    Rule(
                        ruleExpires = referenceEpochSeconds + 60,
                        iss = listOf(StringEquals("bad-iss.mfrancza.com"))
                    )
                ),
                timestamp = referenceEpochSeconds
            )
        }.validate()

        val jwtCredential = JWTCredential(object : Payload {
            override fun getIssuer() = "bad-iss.mfrancza.com"

            override fun getSubject() = "mfrancza"

            override fun getAudience() = listOf("aud.mfrancza.com")

            override fun getExpiresAt() = Date()

            override fun getNotBefore() = Date()

            override fun getIssuedAt() = Date()

            override fun getId() = "mfrancza-id"

            override fun getClaim(name: String?): Claim {
                TODO("Not used")
            }

            override fun getClaims(): MutableMap<String, Claim> {
                TODO("Not used")
            }

        })

        val call = object : ApplicationCall {
            override val application: Application
                get() = TODO("Not needed")
            override val attributes: Attributes
                get() = TODO("Not needed")
            override val parameters: Parameters
                get() = TODO("Not needed")
            override val request: ApplicationRequest
                get() = TODO("Not needed")
            override val response: ApplicationResponse
                get() = TODO("Not needed")

        }

        val result = runBlocking { validate.invoke(call, jwtCredential) }
        assertEquals(null, result, "validate should return null if a rule is met")
    }

    @Test
    fun validateReturnsJWTPrincipalByDefaultWhenNotMet() {
        val validate = RevocationValidateFactory {
            RuleSet(
                rules = listOf(
                    Rule(
                        ruleExpires = referenceEpochSeconds + 60,
                        iss = listOf(StringEquals("bad-iss.mfrancza.com"))
                    )
                ),
                timestamp = referenceEpochSeconds
            )
        }.validate()

        val jwtCredential = JWTCredential(object : Payload {
            override fun getIssuer() = "good-iss.mfrancza.com"

            override fun getSubject() = "mfrancza"

            override fun getAudience() = listOf("aud.mfrancza.com")

            override fun getExpiresAt() = Date()

            override fun getNotBefore() = Date()

            override fun getIssuedAt() = Date()

            override fun getId() = "mfrancza-id"

            override fun getClaim(name: String?): Claim {
                TODO("Not used")
            }

            override fun getClaims(): MutableMap<String, Claim> {
                TODO("Not used")
            }

        })

        val call = object : ApplicationCall {
            override val application: Application
                get() = TODO("Not needed")
            override val attributes: Attributes
                get() = TODO("Not needed")
            override val parameters: Parameters
                get() = TODO("Not needed")
            override val request: ApplicationRequest
                get() = TODO("Not needed")
            override val response: ApplicationResponse
                get() = TODO("Not needed")

        }

        val result = runBlocking { validate.invoke(call, jwtCredential) }
        assertEquals(jwtCredential.payload, (result as JWTPrincipal).payload, "validate should return a matching JWTPrincipal by default if a rule not is met")
    }

    @Test
    fun validateCallsAdditionalValidateWhenNotMet() {
        val testPrincipal = object : Principal {}

        val validate = RevocationValidateFactory {
            RuleSet(
                rules = listOf(
                    Rule(
                        ruleExpires = referenceEpochSeconds + 60,
                        iss = listOf(StringEquals("bad-iss.mfrancza.com"))
                    )
                ),
                timestamp = referenceEpochSeconds
            )
        }.validate { testPrincipal }


        val jwtCredential = JWTCredential(object : Payload {
            override fun getIssuer() = "good-iss.mfrancza.com"

            override fun getSubject() = "mfrancza"

            override fun getAudience() = listOf("aud.mfrancza.com")

            override fun getExpiresAt() = Date()

            override fun getNotBefore() = Date()

            override fun getIssuedAt() = Date()

            override fun getId() = "mfrancza-id"

            override fun getClaim(name: String?): Claim {
                TODO("Not used")
            }

            override fun getClaims(): MutableMap<String, Claim> {
                TODO("Not used")
            }

        })

        val call = object : ApplicationCall {
            override val application: Application
                get() = TODO("Not needed")
            override val attributes: Attributes
                get() = TODO("Not needed")
            override val parameters: Parameters
                get() = TODO("Not needed")
            override val request: ApplicationRequest
                get() = TODO("Not needed")
            override val response: ApplicationResponse
                get() = TODO("Not needed")

        }

        val result = runBlocking { validate.invoke(call, jwtCredential) }
        assertEquals(testPrincipal, result, "validate should return the result of additionalValidation if a rule not is met")
    }
}