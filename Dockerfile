# =================================================================
# ===              DOCKERFILE UNIVERSAL Y OPTIMIZADO            ===
# =================================================================
# Define un proceso de construcción de dos etapas para crear imágenes
# ligeras y seguras.
# =================================================================

FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Argumento para recibir el token de CodeArtifact desde GitHub Actions.
ARG CODEARTIFACT_AUTH_TOKEN

# ▼▼▼ BLOQUE CORREGIDO ▼▼▼
# 1. Crea el directorio .m2 si no existe.
# 2. Crea el archivo settings.xml usando la URL de tu NUEVA cuenta (614520203803).
RUN mkdir -p /root/.m2 && \
    echo '<settings>\
      <servers><server><id>codeartifact</id><username>aws</username><password>${env.CODEARTIFACT_AUTH_TOKEN}</password></server></servers>\
      <profiles><profile><id>codeartifact</id><activation><activeByDefault>true</activeByDefault></activation><repositories><repository><id>codeartifact</id><url>https://pillihuamanlib-614520203803.d.codeartifact.us-east-1.amazonaws.com/maven/pillihuaman-com-pe-lib/</url></repository></repositories></profile></profiles>\
      <activeProfiles><activeProfile>codeartifact</activeProfile></activeProfiles>\
      <mirrors><mirror><id>codeartifact</id><name>CodeArtifact</name><url>https://pillihuamanlib-614520203803.d.codeartifact.us-east-1.amazonaws.com/maven/pillihuaman-com-pe-lib/</url><mirrorOf>external:*</mirrorOf></mirror></mirrors>\
    </settings>' > /root/.m2/settings.xml

# Copia el pom.xml para aprovechar el caché de capas de Docker.
COPY pom.xml .

# Descarga todas las dependencias.
RUN mvn dependency:go-offline --no-transfer-progress

# Copia el resto del código fuente.
COPY src ./src

# Compila la aplicación y crea el .jar.
RUN mvn clean install -DskipTests --no-transfer-progress


# --- ETAPA 2: FINAL (FINAL STAGE) ---
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

# ▼▼▼ ¡ACCIÓN REQUERIDA! ▼▼▼
# Cambia el nombre del .jar para que coincida con tu microservicio.
# Ejemplo para 'security-app':
COPY --from=build /app/target/pillihuaman-com-pe-security-0.0.1-SNAPSHOT.jar app.jar

# ▼▼▼ ¡ACCIÓN REQUERIDA! ▼▼▼
# Cambia el puerto para que coincida con tu microservicio.
# Ejemplo para 'security-app':
EXPOSE 8085

# Comando para ejecutar tu aplicación.
ENTRYPOINT ["java", "-jar", "app.jar"]