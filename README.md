https://docs.spring.io/spring-security/site/docs/3.0.x/reference/security-filter-chain.html

http://docs.spring.io/spring-security/site/docs/3.0.x/reference/ns-config.html

http://docs.spring.io/spring-security/site/docs/3.2.2.RELEASE/apidocs/org/springframework/security/web/util/AntPathRequestMatcher.html

The Security Filter Chain
Spring Security's web infrastructure is based entirely on standard servlet filters. It doesn't use servlets or any other servlet-based frameworks (such as Spring MVC) internally, so it has no strong links to any particular web technology. It deals in HttpServletRequests and HttpServletResponses and doesn't care whether the requests come from a browser, a web service client, an HttpInvoker or an AJAX application.

Spring Security maintains a filter chain internally where each of the filters has a particular responsibility and filters are added or removed from the configuration depending on which services are required. The ordering of the filters is important as there are dependencies between them. If you have been using namespace configuration, then the filters are automatically configured for you and you don't have to define any Spring beans explicitly but here may be times when you want full control over the security filter chain, either because you are using features which aren't supported in the namespace, or you are using your own customized versions of classes.



Filter Ordering
The order that filters are defined in the chain is very important. Irrespective of which filters you are actually using, the order should be as follows:
http://www.quanlei.com/wp-content/uploads/2010/05/Spring_Filter_Order.png



To enable Spring Security, we need add the following jar as Maven dependency to the project:

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-core</artifactId>
    <version>3.0.5.RELEASE</version>
    <type>jar</type>
    <scope>compile</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-web</artifactId>
    <version>3.0.5.RELEASE</version>
    <type>jar</type>
    <scope>compile</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-config</artifactId>
    <version>3.0.5.RELEASE</version>
    <type>jar</type>
    <scope>compile</scope>
</dependency>

Then we need to create spring-security.xml configuration file to manage all beans related to the security aspect.

In the web.xml file, Spring’s DelegatingFilterProxy class should be declared as a servlet filter
<filter>
    <filter-name>filterChainProxy</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>

<filter-mapping>
    <filter-name>filterChainProxy</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
<filter-mapping>
    <filter-name>ndcLogFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>

 the property <filter-name> should point to a valid bean id in the Spring context.

III Security Chain
 Let’s have a closer look into the spring-security.xml configuration file.
<bean id="filterChainProxy" class="org.springframework.security.web.FilterChainProxy">
    <sec:filter-chain-map path-type="ant">
        <sec:filter-chain pattern="/webServices/**" filters="
               securityContextPersistenceFilterForWebServices,
               WSAuthenticationFilter,
               exceptionTranslationFilter,
               filterSecurityInterceptor" />
        <sec:filter-chain pattern="/**"  filters="
               securityContextPersistentFilter,
               logoutFilter,
               authenticationProcessingFilter,
               anonymousFilter,
               exceptionTranslationFilter,
               filterSecurityInterceptor" />
    </sec:filter-chain-map>
</bean>

Next, the <filter-chain-map> allows to match a particular path pattern agains a security filter chain defined in <filter-chain> tag.
The path pattern can be expressed using Ant style or regular expression and is configured by the propery path-type.
You can set as many filter chains as there are different path patterns. In the above configuration, we have a filter chain dedicated to Web Services calls and a generic filter chain for all other requests.

Let’s examine the generic filter chain in depth.
securityContextPersistentFilter: this filter is used to store and retrieve the security context (user credentials, if any) between successive accesses to the application
logoutFilter: this filter handles the logout. It should be placed at the beginning of the filter chain so a click on the logout link (or button) will not go through the rest of the chain
authenticationProcessingFilter: this filter handles all the authentication process
anonymousFilter: this filter handles anonymous login and creates an Authentication object in the HTTP session for later use
exceptionTranslationFilter: this filter re-direct the user to an error page when Security exception is encountered
filterSecurityInterceptor: this filter is manaing the access management
