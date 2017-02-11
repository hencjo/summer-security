# 1.2

## Breaking Changes
* Authenticator is deprecated in favor of CredentialsAuthenticator.
  Use: authenticator.asAuthenticator2() as an adapter.
* Requires Java 8.
* SummerAuthenticatedUser is deleted.
    Summer Security 1.2 provides a set of functions from HttpServletRequest -> Credentials and Credentials -> String (Username).
    These can be combined to take a HttpRequest and return the Username. It removes the need to use HTTP Request Attributes.

## New features
* Client-Side Sessions adhering to RFC6896.
