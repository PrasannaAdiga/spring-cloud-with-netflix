# spring-cloud-with-netflix
spring boot:2.3.6.RELEASE and spring cloud: Hoxton.SR3

# server-discovery 
Service registry pattern - By using netflix Eureka Server
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
Distributed configuration management pattern - Registered with discovery server - By using spring cloud config
  - Add 'spring-cloud-config-server' and 'spring-cloud-starter-netflix-eureka-client' dependencies
  - Add annotations '@EnableConfigServer' and '@EnableEurekaClient' to main application class
  - Add configurations to bootstrap.yml file either to connect Git or local folder path
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
API gateway service - registered with discovery server - running on port 8080 - By using netflix Zuul plugin which by default uses Ribbon as a client side load balancer
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
Running on port 8090 - Microservice developed by using spring boot
  - Service to manage accounts of a customer. Each account belongs to a single customer.
  - Registered with discovery server
  - Fetch configuration details from config server
  - Runs with in memory account details
  
# service-customer
Running on port 8091 - Microservice developed by using spring boot
  - Service to manage each customer. Each customer can have multiple accounts.
  - Registered with discovery server
  - Fetch configuration details from config server
  - Runs with in memory account details
  - Fetches list of account details of a customer from account-service through spring cloud Feign Clients

# service-product
Running on port 8092 - Microservice developed by using spring boot
  - Service to manage each product
  - Registered with discovery server
  - Fetch configuration details from config server
  - Runs with in memory product details
  
# service-order
Running on port 8093 - Microservice developed by using spring boot
  - Service to manage each order. Each order will be created by a customer with their specific account by adding single or multiple products.
  - Registered with discovery server
  - Fetch configuration details from config server
  - Fetches customer information along with their corresponding list of accounts from customer-service
  - Fetches each product information from product-service
  - Keeps in memory order details
  
# To Containerize and run microservices  
  - cd to root folder
  - Run the script file './package-projects.sh'. Running this script file will produce the docker images of each microservices
  - To run each microservice - 'docker-compose up -d' 

# Others
### Docker
To containerize and run different microservices 

### Buildpacks
  - To create docker image for each microservice without using any plugin like 'JIB' or without having any Dockerfile, use the technique Buildpack. By default, the latest spring boot provides support for building docker images through Buildpacks by running the command './gradlew bootBuildImage'

### Docker Compose
  - To run each microservices by using already existing docker images and setting up other required configurations  

### Spring Profile
To provide separate configurations for different environments
  - Create different configurations for each profile, either in config-server or in each micro services
  - Create one such profile for Docker, where service's host can be given as each service-name instead of 'localhost', so that services can be discoverable inside docker containers
    ```
      ---
      spring:
        profiles: docker
      eureka:
        client:
          serviceUrl:
            defaultZone: http://server-discovery:8082/eureka/
    ```
  - Profiles can be activated for each services in docker-compose.yml file through environment variable
    ```
      environment:
        SPRING_PROFILES_ACTIVE: docker  
    ```  

### Spring Boot Actuator
To automate providing infrastructure and application specific metrics data
  - Add the dependency 'spring-boot-starter-actuator'
  - By default, this plugin activates only 'health' and 'info' endpoints
  - To add build related information in to 'info' endpoint, add the below to build.gradle file of each micro services
    ```
      springBoot {
        buildInfo()
      }
    ```
    which produces the info endpoint as 
    ```
      {
        "build": {
          "artifact": "service-product",
          "name": "service-product",
          "time": "2020-12-04T05:47:02.805Z",
          "version": "0.0.1-SNAPSHOT",
          "group": "com.learning.cloud"
        }
      }
    ```
  - To activate all other endpoints provided by the actuator plugin, use the below configuration 
    ```
      management:
        endpoints:
          web:
            exposure:
              include: "*"
        enpoint:
          health:
            show-details: always #To show other details in health endpoint 
    ```
  - View the helath information at 'http://host:port/actuator/health' and information at 'http://host:port/actuator/info'

### Prometheus
To alert and monitoring system metrics
  - Add the dependency 'micrometer-registry-prometheus'
  - Add the below configurations to expose the metrics endpoint by spring boot actuator
    ```
      management:
        endpoints:
          web:
            exposure:
              include: "*"
        enpoint:
          health:
            show-details: always
    ``` 
  - With the above plugin in place, micrometer generates the metrics in the format as required by the Prometheus
  - View the metrics related to Prometheus at 'http://host:port/actuator/prometheus'

### Logback
To write application logs into a file
  - Add the dependency 'logstash-logback-encoder', which converts the text format application logs to json format which is required for the Logstash
  - Add the logback configuration file 'logback-spring.xml' in the application resource path to store the application logs into either file/console/send it directly to Logstash
  Note: Use the absolute path for the file location instead of relative path

### Logging
  - Use the Lombok annotation @Slf4j in each java classes wherever we need to write logs
  - Use log level 'debug' only when we need to log values of some variable in a complex logic
  - Use the log level 'info' whenever some new logic is started or finished with proper input or output values. Also, to log request/response values whenever system calls any external servers
  - Use the log level 'warning' in situations where the code execution might cause some side effect later
  - Use the log level 'error' in catch blocks
  - By default, set the log level as 'error' for the ROOT log
  - Also set the log level as info for application's root package, if there are not so many info logs exists in code
  - Note that, by default if we activate the endpoint 'logger' of spring boot actuator, then the actuator provides a REST endpoint through which we can chang the log level of any package or plugin without restarting the server. We can make this just by executing the API with required data.
  - Enhance the logging with MDC (Mapped Diagnostic Context): 
  
### Validation and Exception Handling
  - Use validator annotations like @NotEmpty, @NotNull, @NotBlank, @Size, @Min, @Max, @Positive etc in the domain/entity classes along with proper exception message details for the user to read
  - In the RestController use the annotation @Validated at the controller level and @Valid at the method level
  - So Hibernate validator will call the validation logic once the API is called and then throws the corresponding exception if the validation fails
  - Create a class with @RestControllerAdvice which extends the ResponseEntityExceptionHandler 
  - The above class will work as GlobalExceptionHandler where we can override any existing spring exception handling logic to provide custom logic, or we can write handler logic for our Custom User Defined exceptions
  - Here we can create exception message object with a meaningful message along with proper error code send it back to user

### Designing REST APIs
  - Use @RestController, @RequestMapping, @PathVariable, @RequestParam, @RequestBody, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping annotations wherver required
  - Use validation related annotations on method arguments, parameters or variables to check the validations
  - Use proper log details by using @Slf4j annotation and handle the global and custom exception messages
  - Use the annotation @RequiredArgsConstructor annotation with private final fields to automatically inject the required beans by spring
  - GET Request: Mainly used for filtering or sorting or searching. To fetch all available records in the pagination format or fetch a record by its id or fetch few records by list of their ids in the pagination format. Response status will be 200 - Ok.
  - Post Request: Post method is used to create a record. Response will be 201 - Created with empty body. Also, return the id of the newly created record as a location header
  - Put Request: Used to update an existing record. Response will be 200 - Ok with the updated record.
  - Delete Request: Used to delete an existing record. Response will be 204 - No Content.

### Filters and Interceptors in Spring
 - Filters which resides basically in a web/servlet containers, are used to filter any request or response which flows between a client, and a servlet which is mapped to a specific URL
 - In case of spring, where we will be having spring IoC container, filters are used between the client and spring MVC dispatcher servlet which is running inside spring container
 - Interceptors are used between spring MVC dispatcher servlet and a specific controllers
 - So Basically Filters are used in a web container outside of spring containers and Interceptors are used inside a spring container. So Interceptor can have complete access on spring context to perform complex logic.
 - Use the spring provided filter OncePerRequestFilter if we want to log any request/response which comes to our application from outside clients. This filter also used to add any custom header to request/response, to deny certain request before it reaches dispatcher servlet, to add authentication check, to log request/response details etc.
 - Use the spring provided interceptor ClientHttpRequestInterceptor if we want to log any request/response which triggers from our application to outside clients by using RestTemplate. Through this interceptor we can also add any custom header to request/response, to deny certain request, to add authentication check, to log request/response objects header, body etc (https://howtodoinjava.com/spring-boot2/resttemplate/clienthttprequestinterceptor/ for reference)
 - If we use Feign to make outside client calls and to log the request and response object details we can use the below Feign bean configuration 
    ```
      @Bean
      Logger.Level feignLoggerLevel() {
          if(log.isDebugEnabled()) {
              return Logger.Level.FULL;
          } else {
              return Logger.Level.BASIC;
          }
      }
    ```
 - Use the spring provided interceptor HandlerInterceptor to get the complete access over any request before it reaches a controller, or after controller return a response and before it render view, or after the view rendered completely. We can usually set start time in the preHandle method and check for the end time in afterCompletion method to find total time taken for an REST API to execute.Also, we can set unique traceId for each request in the prehandle method and later in the afterCompletion method we can remove these traceId
 - Interceptor to work, it must registered in InterceptorRegistry. For this spring provides a configurer class WebMvcConfigurer, addInterceptor method where new interceptors can be registered in the order.
 - Each Interceptor can also configure to get activated only for specific set of URL's
 - ClientHttpRequestInterceptor to work, it must be set as interceptor to RestTemplate by using restTemplate.setInterceptor method
 - OncePerRequestFilter to work, it must be set as filter to FilterRegistrationBean by using its setFilter method

### Spring Security
To provide authentication, authorization
 - Add the dependency 'spring-boot-starter-security'
 - By default, spring security generates a password while running the server. The default username is 'user'. Both of these can be configured through a property file
 - Also, by default spring security enables form based and http basic authentication based authentication flow
 - The above behaviour can be customized by extending the WebSecurityConfigurerAdapter class and overriding it's methods
 - In the above custom class, we can configure Authentication Manager to read the user credentials and roles either from in memory or from databases through custom UserDetailsService and UserDetail classes
 - Also, we can configure which all APIs can be permitted to access and which all APIs are restricted to access
 - APIs which are restricted to access, needs a valid username and password to access. 
 - Once Authentication is successful, we can restrict accessing each APIs only through a valid Role. This process is called authorization
 - We can configure authorization either in the configuration class or at each class/method level through @PreAuthorize or other annotations
 - Also, we can write custom exception handler logic for Unauthorized and Access Denied exceptions
 
### Spring Docs OpenAPI
To automate the generation of API documentation
  - Add the dependencies 'springdoc-openapi-ui' and 'springdoc-openapi-webmvc-core'
  - Run the spring boot application
  - Access the yml version of api doc at 'host:port/v3/api-docs'
  - Access the html version of api doc at 'host:port/swagger-ui.html'
  - Adding ResponseStatus with right status code in each controller's method and each method of @ControllerAdvice will automatically create the right response codes in the doc.
  - Use the annotations @Operation, @Parameter or @ApiResponse to provide additional details in the document or to provide response details manually
  - https://www.baeldung.com/spring-rest-openapi-documentation for reference
  - To show actuator endpoints in the document we can use the below config
    ```
      springdoc:
        show-actuator: true
    ```
  - OpenAPI can be customized by the following configurations:
    ```
      @Bean
      public OpenAPI customOpenAPI() {
          List servers = new ArrayList<Server>();
          servers.add(new Server().url("http://localhost:8090").description("Development server"));

          return new OpenAPI().components(new Components()).info(new Info()
                  .description("<p>Provides list of REST APIs for User Account</p>")
                  .title("API documentation for Account Service").version("1.0.0")).servers(servers);
      }
    ```  
  - To generate the json format of swagger document through gradle add the below plugins, gradle configurations and then run the command 'gradle clean generateOpenApiDocs'. Later this json file can be imported to Swagger Online Editor and save as PDF file.
    ```
      id "com.github.johnrengelman.processes" version "0.5.0"
      id "org.springdoc.openapi-gradle-plugin" version "1.3.0"

      openApi {
        apiDocsUrl.set("http://localhost:8090/v3/api-docs")
        outputDir.set(file("$buildDir/docs"))
        outputFileName.set("account-service.json")
        waitTimeInSeconds.set(10)
      }
    ```  

### Spring Retry 
To run a microservice, after its dependent micro services are ready
  - Use spring retry plugin, to make each microservice to retry connecting to other dependent micro serivces until the dependent micro service is up and healthy. Spring retry plugin provides many configurations which we can use.

### Spring Cloud OpenFeign
To intercommunicate between each micro services 
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
  - Also increase the connection and read timeout of feign client, so that connection timeout error can be solved, while communicating between services
    ```
      feign:
        client:
          config:
            default:
              connectTimeout: 160000000
              readTimeout: 160000000
    ```

### Lombok
To add the logger details in each microservice
  - Use the Lombok annotation @Slf4j
  - create ObjectMapper in each class, wherever log details are needed
  - Use methods in the log objects
    ```
      log.info("Products found: {}", objectMapper.writeValueAsString(products));
    ```
### Spring developer tools
To help local development
  - Lombok
  - spring boot devtools - an automatic restart of server on code changes
  - spring boot configuration processor - helps developers in providing available configuration options in yml/properties files
    
### Cross Cutting Concerns
 - Externalize configurations: Can be achieved by using multiple yml/properties files with Spring Cloud Config  or Consul Config
 - Logging: Can be implemented by using Logback configuration files(By default spring boot supports it) and then send these log details to Elastic Search with the help of Logstash and finally can be visualized each log details by using Kibana
 - Exception Handling: Can be implemented with the help of annotations provided by 'spring-boot-starter-validation' plugin and Global exception handler in Spring Boot (Refer: https://devwithus.com/exception-handling-for-rest-api-with-spring-boot/)
 - Security(Authentication): Can be implemented by the support available in spring-boot-starter-security plugin
 - Security(Authorization): Can be implemented by OAuth2 Authorization server along with spring boot security and JWT
 - Alerts and Monitoring: Spring boot actuator along with MicroMeter provides many infrastructures and application related metrics, which can be sent to Prometheus easily and later can be visualized in Grafana
 - Distributed Tracing: Each API request can be traced across multiple microservices easily with the help of Correlation ID provided by tools like Zipkin and Sleuth
 - API Documentation: By using Swagger or Spring Docs Open API or Spring Rest Docs
 - All the above cross-cutting concerns can also be implemented at API Gateway server(Basically to requests coming from client to server) or by using Service Mesh/sidecar proxy tool(Requests coming from one server to another)

 ### Microservice Patterns
 - Service Discovery or Service Registry: By using spring cloud Netflix Eureka or Consul Service Discovery
 - Distributed Configuration: Configurations of each microservice can be externalized by using spring cloud config or consul config
 - API Gateway or Gateway server: By using Spring cloud netflix Zuul or Spring Cloud API Gateway or Kong API Gateway
 - Circuit Breaker: By using Ribbon or Resilience4J
 - Client side load balancing: By using Hystrix or Spring cloud load balancer

 ### Design Pattern
 - Interface driven REST Controllers: https://www.baeldung.com/spring-interface-driven-controllers
