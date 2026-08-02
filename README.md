
# Hotel Management System

## Përshkrimi

Hotel Management System është një REST API e zhvilluar me Spring Boot për menaxhimin e hoteleve, dhomave, klientëve dhe rezervimeve.

Projekti implementon arkitekturën Layered Architecture dhe përfshin:

- Spring Boot
- Spring Security + JWT Authentication
- Spring Data JPA
- Hibernate
- MySQL
- Log4j2
- Swagger OpenAPI
- Bean Validation
- Exception Handling
- Unit Tests për Log4j2

---

# Teknologjitë

- Java 17
- Spring Boot 4.1.0
- Spring Security
- JWT (Json Web Token)
- Spring Data JPA
- Hibernate
- MySQL
- Log4j2
- Swagger OpenAPI 3
- Maven
- Lombok
- JUnit 5

---

# Struktura e Projektit

```text
hotel
│
├── HotelApplication
│
├── config
│   ├── OpenApiConfig
│   └── SecurityConfig
│
├── controller
│   ├── AuthController
│   ├── HotelController
│   ├── RoomController
│   ├── GuestController
│   ├── GuestProfileController
│   └── ReservationController
│
├── dto
│   ├── request
│   │   ├── LoginRequest
│   │   └── RegisterRequest
│   │
│   ├── response
│   │   └── TokenResponse
│   │
│   ├── HotelDto
│   ├── RoomDto
│   ├── GuestDto
│   ├── GuestProfileDto
│   ├── ReservationDto
│   ├── ReservationResponseDto
│   ├── ReservationStatusUpdateDto
│   └── RoomStatusUpdateDto
│
├── entity
│   ├── AppUser
│   ├── Hotel
│   ├── Room
│   ├── Guest
│   ├── GuestProfile
│   ├── Reservation
│   ├── Role
│   ├── RoomType
│   ├── RoomStatus
│   └── ReservationStatus
│
├── exception
│   ├── GlobalExceptionHandler
│   ├── ResourceNotFoundException
│   ├── DuplicateEmailException
│   ├── DuplicateRoomNumberException
│   ├── RoomCapacityExceededException
│   └── RoomNotAvailableException
│
├── repo
│   ├── AppUserRepo
│   ├── HotelRepo
│   ├── RoomRepo
│   ├── GuestRepo
│   ├── GuestProfileRepo
│   └── ReservationRepo
│
├── security
│   ├── CustomUserDetailsService
│   │
│   └── jwt
│       ├── JwtAuthenticationFilter
│       └── JwtService
│
├── service
│   ├── AuthService
│   ├── HotelService
│   ├── HotelServiceImpl
│   ├── RoomService
│   ├── RoomServiceImpl
│   ├── GuestService
│   ├── GuestServiceImpl
│   ├── GuestProfileService
│   ├── GuestProfileServiceImpl
│   ├── ReservationService
│   └── ReservationServiceImpl
│
├── util
│   └── DemoUserSeeder
│
├── resources
│   ├── application.properties
│   └── log4j2-spring.xml
│
└── test
    └── java
        └── com.lhind.hotel
            ├── logging
            │   ├── LogTestHelper
            │   ├── PriorityLevelTest
            │   ├── RuntimeConfigTest
            │   ├── MultipleDestinationsTest
            │   └── IndependentLoggersTest
            │
            └── HotelApplicationTests
```

---

# Arkitektura

```text
                   Client
                      │
        ┌─────────────┼─────────────┐
        │             │             │
        ▼             ▼             ▼
    Browser       Postman      Swagger UI
                      │
                      ▼
            JwtAuthenticationFilter
                      │
                      ▼
               Spring Security
                      │
                      ▼
               Controller Layer
                      │
                      ▼
                Service Layer
                      │
                      ▼
              Repository Layer
                      │
                      ▼
                 MySQL Database
```

---

# Funksionalitetet

## Authentication

- Register
- Login
- JWT Token
- Role Based Authorization

---

## Hotel Management

- Create Hotel
- Update Hotel
- Delete Hotel
- Find Hotel
- Search by City

---

## Room Management

- Create Room
- Update Room
- Delete Room
- Change Room Status
- Search by Hotel
- Search by Status

---

## Guest Management

- Create Guest
- Update Guest
- Delete Guest
- Search Guest
- Search by Email

---

## Guest Profile

- Create Profile
- Update Profile
- Delete Profile
- Search Profile

---

## Reservation Management

- Create Reservation
- Update Reservation
- Cancel Reservation
- Delete Reservation
- Search Reservation
- Search by Guest
- Search by Room

---

# Security

Projekti përdor:

- Spring Security
- JWT Authentication
- Role Based Authorization

Rolet:

- ADMIN
- RECEPTIONIST
- GUEST

JWT filtrohet nga:

- JwtAuthenticationFilter

Autentikimi realizohet nga:

- CustomUserDetailsService

---

# Swagger

Swagger është i integruar për dokumentimin e API-ve.

Pas nisjes së aplikacionit:

```
http://localhost:8080/swagger-ui/index.html
```

Dokumentacioni OpenAPI:

```
http://localhost:8080/v3/api-docs
```

---

# Log4j2

Projekti përdor Log4j2 për logimin e aplikacionit.

Konfigurimi ndodhet tek:

```
src/main/resources/log4j2-spring.xml
```

Logger janë implementuar në:

- Controllers
- Services
- Security
- Exception Handler
- Utility Classes

Log Levels:

- TRACE
- DEBUG
- INFO
- WARN
- ERROR
- FATAL

---

# Testimi i Log4j2

Janë implementuar testet:

- PriorityLevelTest
- RuntimeConfigTest
- MultipleDestinationsTest
- IndependentLoggersTest
- LogTestHelper

Këto teste verifikojnë:

- Prioritetet e logimit
- Runtime Configuration
- Multiple Appenders
- Logger Independence

---

# Konfigurimi

application.properties

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

---

# Përdorimi

## 1. Klono projektin

```bash
git clone <repository-url>
```

---

## 2. Hyr në projekt

```bash
cd hotel
```

---

## 3. Instalo varësitë

```bash
mvn clean install
```

---

## 4. Nise aplikacionin

```bash
mvn spring-boot:run
```

ose

Run **HotelApplication** nga IntelliJ IDEA.

---

# Testimi me Swagger

1. Register

```
POST /auth/register
```

2. Login

```
POST /auth/login
```

3. Kopjo JWT Token.

4. Kliko **Authorize** në Swagger.

5. Shkruaj:

```
Bearer <JWT_TOKEN>
```

6. Tani mund të aksesosh endpoint-et sipas rolit.

---

# Autor
Kristi Gjosasi
Projekti u zhvillua si pjesë e Internship Java  duke përdorur Spring Boot, JWT Authentication, Swagger dhe Log4j2.