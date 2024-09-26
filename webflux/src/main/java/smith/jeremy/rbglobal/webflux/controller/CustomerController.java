package smith.jeremy.rbglobal.webflux.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import smith.jeremy.rbglobal.webflux.repository.Customer;
import smith.jeremy.rbglobal.webflux.repository.CustomerRepository;

import java.util.Objects;

@RestController
@RequestMapping(value="/api")
@Slf4j
@AllArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Operation(summary="Retrieve all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved the customer list",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)) }),
            @ApiResponse(responseCode = "401", description = "Request is unauthorized",
                    content = @Content)
    })
    @GetMapping(path = "/customers",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Created the customer",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Customer.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid customer data",
                content = @Content),
        @ApiResponse(responseCode = "401", description = "Request is unauthorized",
                content = @Content)
    })
    @PostMapping(path="/customers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @Operation(summary = "Retrieve customer with the specified id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved the customer specified",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)) }),
            @ApiResponse(responseCode = "401", description = "Request is unauthorized",
                    content = @Content)
    })
    @GetMapping(path="/customers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Customer> getCustomer(@PathVariable("id") Long id) {
        return customerRepository.findById(String.valueOf(id));
    }

    @Operation(summary = "Update the customer ith the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the customer",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid customer data",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Request is unauthorized",
                    content = @Content)
    })
    @PutMapping(path="/customers/{id}",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE )
    public Mono<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        // confirm the ID in our path and the ID on the customer object match
        try {
            if (Objects.equals(id, customer.getId())) {
                return customerRepository.save(customer);
            }
        } catch (Exception e) {
            log.info("Exception: {}", e.getMessage());
            return Mono.error(e);
        }
        return Mono.empty();
    }
}
