# jwt-revocation-ktor-server-auth

Library to allow using a RuleSet source to validate JWTs with io.ktor:ktor-server-auth-jwt.

Use with jwt-revocation-ruleset-client to retrieve a RuleSet from a server.

## How to use

### Import Dependency

```kotlin
implementation("com.mfrancza:jwt-revocation-ktor-server-auth:$version")
```

### Set Up A RuleSet Source

The source of the ruleSet used to check for token revocation is not coupled to the library for applying them.

The jwt-revocation-ruleset-client library provides a client for obtaining a RuleSet from an HTTP endpoint, such as the one exposed by the manager at GET /ruleset.

### Check if the Credential is Revoked

In the validate lambda for the Authentication plugin, call the notRevoked method added to the JWTCredential.  This method returns a JWTCredential or null, facilitating the chaining of validation calls.

```kotlin
install(Authentication) {
    jwt("auth-jwt") {
        validate {
            it.notRevoked(source.ruleSet())?.toPrincipal()
        }
    }
}
```
