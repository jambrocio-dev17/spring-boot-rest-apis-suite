# Spring APIs Lab — Laboratorio V

Laboratorio de APIs REST independientes desarrolladas con **Spring Boot** y **Maven**, aplicando los conceptos de HTTP, JSON, controladores REST y operaciones CRUD utilizando listas en memoria (sin base de datos).

## Objetivo

Desarrollar 10 APIs REST independientes, cada una administrando un recurso distinto mediante operaciones CRUD completas (`GET`, `POST`, `PUT`, `PATCH`, `DELETE`), reforzando el uso de Spring, Postman y control de versiones con Git.

## Tecnologías

- Java
- Spring Boot (Spring Web)
- Maven
- Postman (pruebas de los endpoints)

## APIs incluidas

| # | Recurso | Endpoint base |
|---|---------|----------------|
| 1 | Productos | `/api/productos` |
| 2 | Estudiantes | `/api/estudiantes` |
| 3 | Libros | `/api/libros` |
| 4 | Empleados | `/api/empleados` |
| 5 | Peliculas | `/api/peliculas` |
| 6 | Cursos | `/api/cursos` |
| 7 | Vehiculos | `/api/vehiculos` |
| 8 | Tareas | `/api/tareas` |
| 9 | Clientes | `/api/clientes` |
| 10 | Pedidos | `/api/pedidos` |

Cada API expone los siguientes endpoints:

```
GET     /api/{recurso}
GET     /api/{recurso}/{id}
POST    /api/{recurso}
PUT     /api/{recurso}/{id}
PATCH   /api/{recurso}/{id}
DELETE  /api/{recurso}/{id}
```

Todas las APIs inician con al menos 5 registros de ejemplo cargados en memoria.

## Estructura del proyecto

```
spring-apis-lab/
│
├── pom.xml
│
└── src/main/java/com/lab/apis/
    │
    ├── SpringApisLabApplication.java
    │
    ├── controller/
    │   └── (10 controladores REST)
    │
    └── model/
        └── (10 clases modelo)
```

## Cómo ejecutar

```bash
cd spring-apis-lab
mvnw.cmd spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

## Pruebas con Postman

Se incluye la colección `APIs REST - LaboratorioV.postman_collection.json` con las 10 carpetas (una por API) y sus 6 peticiones correspondientes. Importarla en Postman para probar todos los endpoints.

## Repositorio

https://github.com/jambrocio-dev17/spring-boot-rest-apis-suite

## Autor

Josue Sebastian Ambrocio Alvarado — Laboratorio V (APIs REST con Spring)# spring-boot-rest-apis-suite
Módulo integral de APIs REST con Spring Boot, operaciones CRUD en memoria y colección Postman.
