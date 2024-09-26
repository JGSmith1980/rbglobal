package smith.jeremy.rbglobal.webflux.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        String authToken = serverWebExchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(authToken) && authToken.equalsIgnoreCase("static_token")){
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken("principal", "credentials",  AuthorityUtils.createAuthorityList("api") );
            return Mono.just(new SecurityContextImpl(authentication));
        }

        return Mono.empty();
    }

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        // We don't need to save any contexts for this exercise, but we still need to define it
        return null;
    }
}
