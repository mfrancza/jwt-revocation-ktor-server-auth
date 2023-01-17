package com.mfrancza.jwtrevocation.ktor.server.auth

import com.mfrancza.jwtrevocation.rules.ClaimsSource
import com.mfrancza.jwtrevocation.rules.RuleSet
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.Principal
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import kotlinx.coroutines.runBlocking

/**
 * Factory for validate functions for use with https://api.ktor.io/ktor-server/ktor-server-plugins/ktor-server-auth-jwt/io.ktor.server.auth.jwt/-j-w-t-authentication-provider/-config/validate.html
 */
class RevocationValidateFactory(private val ruleSetSource : suspend () -> RuleSet) {

    private var ruleSet = runBlocking { ruleSetSource.invoke() }

    /**
     * Adapter to provide a ClaimsSource implementation for JWTCredential
     */
    private class JWTCredentialClaimSource(val credential: JWTCredential) : ClaimsSource {
        override fun audValue(): String? = credential.audience.firstOrNull()

        override fun expValue(): Long? = credential.expiresAt?.toInstant()?.epochSecond

        override fun iatValue(): Long? = credential.issuedAt?.toInstant()?.epochSecond

        override fun issValue(): String? = credential.issuer

        override fun jtiValue(): String? = credential.jwtId

        override fun nbfValue(): Long? = credential.notBefore?.toInstant()?.epochSecond

        override fun subValue(): String? = credential.subject

    }

    /**
     * Returns function for use with https://api.ktor.io/ktor-server/ktor-server-plugins/ktor-server-auth-jwt/io.ktor.server.auth.jwt/-j-w-t-authentication-provider/-config/validate.html
     * @param additionalValidate takes a validate function with the same signature as the parameter of the JWTAuthenticationProvider function so validate functions can be chained
     */
    fun validate(additionalValidate : (suspend ApplicationCall.(JWTCredential) -> Principal?) =
                     {credential -> JWTPrincipal(credential.payload)}
    ) : suspend ApplicationCall.(JWTCredential) -> Principal? =
        { credential ->
            if (!ruleSet.isMet(JWTCredentialClaimSource(credential))) {
                this.additionalValidate(credential)
            }
            else {
                null
            }
    }
}

