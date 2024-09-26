package smith.jeremy.rbglobal.webflux;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import smith.jeremy.rbglobal.webflux.security.SecurityContextRepository;

@TestConfiguration
public class WebFluxControllerSecurityTestConfig {

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http, SecurityContextRepository securityContextRepository) {
        // allow authenticated on api calls

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(securityContextRepository)
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
                .authorizeExchange(exchange -> exchange.anyExchange().authenticated())
                .httpBasic(Customizer.withDefaults()).build();
    }
}
