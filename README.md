# spring-cloud-with-netflix
spring boot:2.3.6.RELEASE and spring cloud: Hoxton.SR3

# server-discovery
Independent service registry - running on port 8082
  - Add 'spring-cloud-starter-netflix-eureka-server' dependency
  - Add annotation '@EnableEurekaServer' to main application class
  - Add below configurations to application.yml file
  ```
      server:
        port: 8082
      spring:
        application:
          name: discovery-server
      eureka:
        client:
          serviceUrl:
            defaultZone: http://localhost:8082/eureka/
          register-with-eureka: false
          fetch-registry: false
  ```
# server-config
Distributed configuration management - Registered with discovery server - running on port 8081
  - Add 'spring-cloud-config-server' and spring-cloud-starter-netflix-eureka-client' dependencies
  - Add annotations '@EnableConfigServer' and '@EnableEurekaClient' to main application class
  - Add configurations to bootstap.yml file either to connect git or local folder path
  ```
      server:
        port: 8081
      spring:
        application:
          name: config-server
        profiles:
          active: native
        cloud:
          config:
            server:
              native:
                searchLocations: classpath:/config-repo
      eureka:
        client:
          serviceUrl:
            defaultZone: http://localhost:8082/eureka/
  ```
  - Add each microservices configuration files under the folder 'config-repo'

# server-gateway
API gateway service - registered with discovery server - running on port 8080
  - Add 'spring-cloud-starter-netflix-zuul', 'spring-cloud-starter-config' and 'spring-cloud-starter-netflix-eureka-client' dependencies
  - Add annotations '@EnableZuulProxy' and '@EnableEurekaClient' to main application class
  - Add below configurations to bootstrap.yml file
  ```
      server:
        port: 8080
      spring:
        application:
          name: gateway-server
        cloud:
          config:
            uri: http://localhost:8081
  ```
# service-account
Running on port 8090
  - Service to manage accounts of a customer. Each account belongs to a single customer
  - Registered with discovery server
  - Fetch configuration details from config server
  - Runs with in memory account details
  
# service-customer
Running on port 8091
  - Service to manage each customer. Each customer can have multiple accounts
  - - Registered with discovery server
  - Fetch configuration details from config server
  - Runs with in memory account details
  - Fetches list of account details of a customer from account-service through spring cloud Feign Clients
  
# Others
### To intercommunicate between micro services use Spring Cloud Feign Clients 
  - Add 'spring-cloud-starter-openfeign' dependency
  - Add annotation '@EnableFeignClients' to main application class
  - Add required Feign Client Classes
    ```
      @FeignClient(name = "account-service")
      public interface AccountServiceClient {
        @GetMapping("/account/customer/{customerId}")
        List<Account> findByCustomerId(@PathVariable("customerId") Long id);
      }
    ```
### To add the logger details in each microservices use Lombok
  - Use the Lombok annotation @Slf4j
  - create ObjetMapper in each class, wherever log details are needed
  - Use methods in the log objects
    ```
      log.info("Products found: {}", objectMapper.writeValueAsString(products));
    ```