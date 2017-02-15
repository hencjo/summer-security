# 1.2

## Breaking Changes
* Authenticator is deprecated in favor of CredentialsAuthenticator.
  Use: authenticator.asAuthenticator2(x -> x) as an adapter.
* Requires Java 8.
* SummerAuthenticatedUser is removed.
    Summer Security 1.2 provides a set of functions from HttpServletRequest -> Credentials and Credentials -> String (Username).
    These can be combined to take a HttpRequest and return the Username. It removes the need to use HTTP Request Attributes.

## New features
* Client-Side Sessions adhering to RFC6896.

## Other changes
* Bundled Base64 Encoder/Decoder removed in favor of Java 8's.
* Budled Charsets removed in favor of Java 8's StandardCharsets.
* Optionality is expressed with Java8's Optional instead of null.
* Using Java 8 Date/Time API in Cookies-utility.  
