## Mover a Core

#### Indexar logs en Elastic (logback - logstash - elastic)

- Dependencia `logstash-logback-encoder`
- Configurar logback `src/resources/logback-spring.xml`
- Configurar `logstash.conf`

#### Integrar Grafana

- Dependencia `spring-boot-starter-actuator`
- Dependencia `micrometer-registry-prometheus`
- Habilitar endpoint `/actuator/prometheus` `(management.endpoints.web.exposure.include=prometheus)`
- Primera password es admin - admin