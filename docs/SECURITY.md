# Security Strategy

## Authentication & Authorization
- **JWT (JSON Web Tokens)**: Used for stateless API authentication.
- **Spring Security**: Configured to protect endpoints (e.g., `/api/v1/devices/**` requires authentication).
- **Password Hashing**: BCrypt.

## Best Practices
- **Secret Management**: Passwords, DB credentials, and JWT secrets MUST be injected via Environment Variables or `application-local.yml` files excluded from Git. NEVER commit secrets.
- **CORS**: Configured strictly to allow only the known frontend origin.
- **Input Validation**: Use Jakarta Validation (`@Valid`, `@NotNull`, etc.) on all DTOs.
