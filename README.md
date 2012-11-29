# Summer Security

Summer Security is a library/DSL for Java Web Applications that need a simpler authentication mechanism. 

It's design goals are:
* Only authentication. Not authorization. Authorization is generally too domain specific to warrant using a library.
* Minimalistic dependencies. Summer Security don't clutter your classpath. And it doesn't depend on any logging framework. The only dependency is the Servlet API.
* Configuration in code, not XML or Annotations. 
* Simple to extend with own Request Mathers and Responders.

