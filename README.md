# Shop API (Spring Boot + PostgreSQL)

Basit e-ticaret backend API’si. Java 17, Spring Boot 3, JPA, PostgreSQL.

## Özellikler
- Users: CRUD (basit)
- Products: CRUD + Validation + Exception Handling
- GlobalExceptionHandler
- (Test) JUnit + veya Testcontainers

## Gereksinimler
- Java 17
- PostgreSQL (local: 5432, db: `shop_api`)
- Maven

### Endpointler
- `GET /api/products`
- `POST /api/products`
- `GET /api/products/{id}`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

## Test
mvn test
