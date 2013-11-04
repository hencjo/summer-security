# Summer Security

Summer Security is a library/DSL for Java Web Applications that need a simpler authentication mechanism.

## Features

- HTTP Basic Authentication (with or without WWW-Authenticate).
- Form Login, compatible with Spring Security.
- No dependencies.
- Configuration in code. (No XML-configuration.)
- No ThreadLocal. The logged in user is a function of the HttpRequest.
- Only authentication. Not authorization.
- Extendable through RequestMatchers and Responders.

## Example

```java
import java.io.IOException;
import javax.servlet.*;
import com.hencjo.summer.security.*;
import com.hencjo.summer.security.api.*;
import static com.hencjo.summer.security.api.Summer.*;

public class AuthenticationFilter extends AbstractFilter {
    private final SummerLogger logger = Loggers.noop();
    private final Authenticator authenticator = Authenticators.allowEveryoneAuthenticator();
	private final ServerSideSession session = new ServerSideSession("username");
	private final HttpBasicAuthenticator httpBasicAuthenticator = new HttpBasicAuthenticator(authenticator, "Realm");
	private final FormBasedLogin formBasedLogin = new FormBasedLogin(logger, authenticator, session.sessionWriter(), 
			"/j_spring_security_check", "/j_spring_security_logout", 
			"j_username", "j_password", 
			redirect("/login.html#?logout=true"), redirect("/login.html#?failure=true"), redirect("/index.html"));

	
	private final SummerFilterDelegate filterDelegate = summer(logger, 
			when(pathBeginsWith("/img/")).thenAllow(),
			when(pathBeginsWith("/lib/")).thenAllow(),
			when(pathEquals("/login.html")).thenAllow(),
			when(pathEquals("/login.js")).thenAllow(),
			when(formBasedLogin.logoutRequest()).then(formBasedLogin.performLogoutRequest()),
			when(formBasedLogin.loginRequest()).then(formBasedLogin.performLoginRequest()),
			when(session.exists()).thenAllow(),
			when(httpBasicAuthenticator.authorizes()).thenAllow(),
			when(header("X-Requested-With").equals("XMLHttpRequest")).then(status(403)),
			otherwise().then(redirect("/login.html"))
		);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		filterDelegate.doFilter(request, response, filterChain);
	}
}
```

Include it in your pom like this:
```xml
<dependency>
    <groupId>com.hencjo.summer</groupId>
    <artifactId>summer-security</artifactId>
    <version>1.1.1</version>
</dependency>
```

Summer Security is free software/open source, and is distributed under the [Apache License 2.0](http://opensource.org/licenses/Apache-2.0).

Summer Security is created and maintained by Henrik Johansson (Twitter: [@hencjo](http://twitter.com/hencjo))
