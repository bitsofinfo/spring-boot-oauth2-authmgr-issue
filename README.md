# spring-boot-oauth2-authmgr-issue

https://gitter.im/spring-projects/spring-security-oauth?at=58b5c68800c00c3d4f9e91d3

`./gradlew bootRun`

Issue:

POSTs to `http://localhost:8080/oauth/token` (basic auth: testuser/123) never are authenticated w/ `MyAuthenticationManagerAndProvider`. The `MyClientDetailsService` is invoked however, but the actual auth for whatever reason always hits the `DoaAuthenticationProvider`

This is a slimmed down sample of a much larger app that worked fine w/ this kind of configuration under spring-boot 1.2.5.

This behavior started after upgrading to 1.3.x, 1.4.x and 1.5.x