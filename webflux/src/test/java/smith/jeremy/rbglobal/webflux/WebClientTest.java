package smith.jeremy.rbglobal.webflux;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import smith.jeremy.rbglobal.webflux.controller.CustomerController;
import smith.jeremy.rbglobal.webflux.exception.ValidationException;
import smith.jeremy.rbglobal.webflux.repository.Customer;
import smith.jeremy.rbglobal.webflux.repository.CustomerRepository;
import smith.jeremy.rbglobal.webflux.security.SecurityContextRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CustomerController.class)
@WebAppConfiguration
@Import(WebFluxControllerSecurityTestConfig.class)
public class WebClientTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private SecurityContextRepository repository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private ConnectionFactoryInitializer initializer;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void createANewCustomer_Successfully() throws ValidationException {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("principal", "credentials",  AuthorityUtils.createAuthorityList("api"));
        SecurityContext testSecurityContext = new SecurityContextImpl(authentication);
        when(repository.load(any())).thenReturn(Mono.just(testSecurityContext));
        Customer customer = new Customer("John", "Johnson", "John@Johnson.com");
        when(customerRepository.save(any(Customer.class))).thenReturn(Mono.just(customer));
        client.post().uri("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "static_token")
                .body(Mono.just(customer), Customer.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .value(resultCustomer -> {
                    assertAll("Verifying we got our customer back",
                            () -> assertNotNull(resultCustomer),
                            () -> assertNotNull(resultCustomer.getEmail()),
                            () -> assertNotNull(resultCustomer.getLastName()),
                            () -> assertNotNull(resultCustomer.getFirstName())
                    );
                });

    }

    @Test
    public void retrieveACustomer_Successfully() throws ValidationException {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("principal", "credentials",  AuthorityUtils.createAuthorityList("api"));
        SecurityContext testSecurityContext = new SecurityContextImpl(authentication);
        when(repository.load(any())).thenReturn(Mono.just(testSecurityContext));
        when(customerRepository.findById(anyString())).thenReturn(Mono.just(new Customer("John", "Johnson", "John@Johnson.com")));
        client.get().uri("/api/customers/1")
                .header("Authorization", "static_token")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .value(resultCustomer -> {
                    assertAll("Verifying we got our customer back",
                            () -> assertNotNull(resultCustomer),
                            () -> assertNotNull(resultCustomer.getEmail()),
                            () -> assertNotNull(resultCustomer.getLastName()),
                            () -> assertNotNull(resultCustomer.getFirstName()));
                });
    }

    @Test
    public void getCustomer_BadToken() {
        Authentication authentication =
                new AnonymousAuthenticationToken("principal", "credentials",  AuthorityUtils.createAuthorityList("api"));
        SecurityContext testSecurityContext = new SecurityContextImpl(authentication);
        when(repository.load(any())).thenReturn(Mono.just(testSecurityContext));
        client.get().uri("/api/customers/1")
                .header("Authorization", "bad_token")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void getCustomer_NoToken(){
        Authentication authentication =
                new AnonymousAuthenticationToken("principal", "credentials",  AuthorityUtils.createAuthorityList("api"));
        SecurityContext testSecurityContext = new SecurityContextImpl(authentication);
        when(repository.load(any())).thenReturn(Mono.just(testSecurityContext));
        client.get().uri("/api/customers/1")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
