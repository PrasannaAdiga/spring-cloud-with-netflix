# spring-cloud-with-netflix
spring boot:2.3.6.RELEASE and spring cloud: Hoxton.SR3

# discovery-server (Independent/clustered service registry - port 8082)
  - Add 'spring-cloud-starter-netflix-eureka-server' dependency
  - Add annotation '@EnableEurekaServer' to main application class
  - Add below configurations to application.yml file
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

# config-server(Distributed configuration management - registered to discovery server - port 8081)
  - Add 'spring-cloud-config-server' and spring-cloud-starter-netflix-eureka-client' dependencies
  - Add annotations '@EnableConfigServer' and '@EnableEurekaClient' to main application class
  - Add configurations to bootstap.yml file either to connect git or local folder path
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
  - Add each microservices configuration files under the folder 'config-repo'

# gateway-server(api gateway service - registered to discovery server - port 8080)
  - Add 'spring-cloud-starter-netflix-zuul', 'spring-cloud-starter-config' and 'spring-cloud-starter-netflix-eureka-client' dependencies
  - Add annotations '@EnableZuulProxy' and '@EnableEurekaClient' to main application class
  - Add below configurations to bootstrap.yml file
      server:
        port: 8080
      spring:
        application:
          name: gateway-server
        cloud:
          config:
            uri: http://localhost:8081

