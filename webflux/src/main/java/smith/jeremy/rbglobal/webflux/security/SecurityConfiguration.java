package smith.jeremy.rbglobal.webflux.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http, SecurityContextRepository securityContextRepository) {
        // allow authenticated on api calls
        return http
              .csrf(CsrfSpec::disable)
            .securityContextRepository(securityContextRepository)
            .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
            .authorizeExchange(exchange -> exchange.anyExchange().authenticated())
            .httpBasic(Customizer.withDefaults()).build();
    }

    @Bean
    SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {
        // deny all other calls
        http
            .authorizeExchange((exchanges) -> exchanges
                .anyExchange().denyAll()
            );
        return http.build();
    }
}
