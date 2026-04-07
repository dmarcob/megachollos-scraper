# Resumen de solucion: error de IT + bonus Kata 1

## Objetivo
Documentar lo que hicimos para:
1. desbloquear el error de entorno en Integration Tests (IT) con Testcontainers/Docker, y
2. completar el bonus de la `Kata1_BrandRepositoryTest`.

---

## 1) Error de IT (Testcontainers + Docker en Windows)

### Sintomas observados
- Fallos tipo:
  - `Could not find a valid Docker environment`
  - `failed to connect to the docker API at npipe:////./pipe/docker_engine`
  - `MalformedChunkCodingException: Bad chunk header`
- En algunos intentos Maven/IDE detectaba Docker y en otros no (fallo intermitente de configuracion de entorno).

### Causa principal
La deteccion de Docker por Testcontainers estaba contaminada por configuracion local de usuario (fuera del proyecto), sobre todo:
- `C:\Users\pulz\.testcontainers.properties` forzando `NpipeSocketClientProviderStrategy`.
- Variables `DOCKER_HOST` / `DOCKER_API_VERSION` forzadas manualmente en algunos intentos.

### Acciones aplicadas
- En el proyecto se forzo ignorar configuraciones de usuario con:
  - `-Ddockerconfig.source=autoIgnoringUserProperties`.
- Se limpio la configuracion global conflictiva:
  - eliminar `C:\Users\pulz\.testcontainers.properties`.
- Se limpiaron variables persistidas de usuario cuando estaban forzadas:
  - `DOCKER_HOST`
  - `DOCKER_API_VERSION`
- Se verifico conectividad Docker y ejecucion del test desde consola/IDE.

### Comandos de referencia usados
```powershell
# Ver archivo global de Testcontainers
$tc = "$HOME\.testcontainers.properties"
if (Test-Path $tc) { Get-Content $tc }

# Eliminar archivo conflictivo
if (Test-Path $tc) { Remove-Item $tc -Force }

# Limpiar variables de usuario
[Environment]::SetEnvironmentVariable('DOCKER_HOST',$null,'User')
[Environment]::SetEnvironmentVariable('DOCKER_API_VERSION',$null,'User')

# Limpiar variables de sesion actual
Remove-Item Env:DOCKER_HOST -ErrorAction SilentlyContinue
Remove-Item Env:DOCKER_API_VERSION -ErrorAction SilentlyContinue

# Verificar Docker
docker version
docker context ls
```

### Resultado
- La deteccion de Docker dejo de fallar por configuracion local contaminada.
- En IntelliJ se observo conexion correcta de Testcontainers (`Found Docker environment...`, contenedor de Postgres arrancado).

> Nota: este problema era de **entorno de ejecucion** (Docker/Testcontainers), no de logica de negocio.

---

## 2) Bonus de la Kata 1

Archivo trabajado:
- `src/test/java/com/megachollos/kata/Kata1_BrandRepositoryTest.java`

### Que se pedia en el bonus
Agregar `shouldFailWhenDuplicateUniqueName()` para demostrar que la base de datos real (PostgreSQL) aplica la restriccion de clave primaria y lanza `DataIntegrityViolationException`.

### Cambios realizados
1. Se dejo la kata ejecutando contra PostgreSQL real con Testcontainers:
   - `@Testcontainers`
   - `@AutoConfigureTestDatabase(replace = NONE)`
   - contenedor `PostgreSQLContainer("postgres:17-alpine")` con `@ServiceConnection`.
2. Se agrego el test bonus `shouldFailWhenDuplicateUniqueName()`.
3. Se inyecto `JdbcTemplate` para forzar un `INSERT` duplicado real en SQL.

### Por que usamos `JdbcTemplate` en el bonus
Con `JpaRepository.save(...)` y misma PK, JPA puede hacer `merge` (update) y no siempre provoca excepcion por duplicado.
Para probar de forma **determinista** la violacion de restriccion, hicimos:
- primer registro con `saveAndFlush`,
- segundo registro con `INSERT` SQL directo (misma PK),
- asercion de `DataIntegrityViolationException`.

### Comando para ejecutar solo la kata
```powershell
.\mvnw.cmd test "-Dtest=Kata1_BrandRepositoryTest" "-Dsurefire.excludes="
```

### Resultado de la kata
- `Kata1_BrandRepositoryTest` ejecuta 2 tests:
  - `shouldSaveAndFindBrand()`
  - `shouldFailWhenDuplicateUniqueName()`
- Ambos en verde cuando Docker/Testcontainers esta disponible.

---

## Conclusiones para el profesor
- Se corrigio una incidencia real de integracion causada por entorno local de Docker/Testcontainers en Windows.
- Se demostro el valor de IT reales frente a mocks: la restriccion de base de datos (duplicado PK) solo se valida correctamente con PostgreSQL real.
- La kata queda con un caso feliz + un caso de integridad de datos reproducible y estable.

