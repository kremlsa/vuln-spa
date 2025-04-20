# 1. Строим фронтенд
FROM node:22-alpine as frontend-builder

WORKDIR /app/frontend

COPY frontend/package*.json ./
RUN npm install

COPY frontend/ .
RUN npm run build

# 2. Строим бэкенд
FROM maven:3.9.6-eclipse-temurin-21 as backend-builder

WORKDIR /app

COPY backend/pom.xml backend/pom.xml
COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY backend/ ./backend/
COPY --from=frontend-builder /app/frontend/build/ ./frontend/build/

RUN mvn clean package -f backend/pom.xml -DskipTests -Pno-frontend

# 3. Финальный образ
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=backend-builder /app/backend/target/backend-*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]