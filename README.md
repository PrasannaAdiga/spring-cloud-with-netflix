# spring-cloud-with-netflix
spring boot:2.3.6.RELEASE and spring cloud: Hoxton.SR3

# server-discovery 
Independent service registry - running on port 8082 - By using netflix Eureka plugin
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
Distributed configuration management - Registered with discovery server - running on port 8081 By using spring cloud config
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
  - Service to manage accounts of a customer. Each account belongs to a single customer
  - Registered with discovery server
  - Fetch configuration details from config server
  - Runs with in memory account details
  
# service-customer
Running on port 8091 - Microservice developed by using spring boot
  - Service to manage each customer. Each customer can have multiple accounts
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
  - Fetches customer informations along with their corresponding list of accounts from customer-service
  - Fetches each product informations from product-service
  - Keeps in memory order details
  
# To Containerize and run microservices  
  - cd to root folder
  - Run the script file './package-projects.sh'. Running this script file will produce the docker images of each microservices
  - To run each microservices - 'docker-compose up -d' 

# Others
### Docker
To containerize and run different microservices 

### Buildpacks
  - To create docker image for each microservices without using any plugin like 'JIB' or without having any Dockerfile, use the technique Buildpack. By default latest spring boot provides support for building docker images through Buildpacks by running the command './gradlew bootBuildImage'

### Docker Compose
  - To run each microservices by using already existing docker images and setting up other required configurations  

### Spring Profile
To provide separate configurations for different environments
  - Create different configurations for each profiles, either in config-server or in each micro services
  - Create one such profile for docker, where service's host can be given as each service-name instead of 'localhost', so that services can be discoverable inside docker containers
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
  - By default this plugin activates only 'health' and 'info' endpoints
  - To add build related informations in to 'info' endpoint, add the below to build.gradle file of each micro services
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
  - Add the dependency 'micrometer-registry-promeheus'
  - Add the below configuraions to expose the metrics endpoint by spring boot actuator
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
  - With the above plugin in place, micrometer generates the metrics in the format as required by the promeheus
  - View the metrics related to promeheus at 'http://host:port/actuator/prometheus'

### Logback
To write application logs into a file
  - Add the dependency 'logstash-logback-encoder', which converts the text format application logs to json foramt which is required for the Logstash
  - Add the logback configuration file 'logback-spring.xml' in the application resource path to store the application logs into either file/console/send it directly to logstash
  Note: Use the absolute path for the file location instead of relative path


### Spring Docs OpenAPI
To automate the generation of API documentation
  - Add the dependencies 'springdoc-openapi-ui' and 'springdoc-openapi-webmvc-core'
  - Run the spring boot application
  - Access the yml version of api doc at 'host:port/v3/api-docs'
  - Access the html version of api doc at 'host:port/swagger-ui.html'

### Spring Retry 
To run a microservice, after its dependent micro services are ready
  - Use spring retry plugin, to make each microservice to retry connecting to other dependent micro serivces until the dependent micro service is up and healthy. Spring retry plugin provides many configurations which we can use

### Feign Client
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
To add the logger details in each microservices
  - Use the Lombok annotation @Slf4j
  - create ObjetMapper in each class, wherever log details are needed
  - Use methods in the log objects
    ```
      log.info("Products found: {}", objectMapper.writeValueAsString(products));
    ```