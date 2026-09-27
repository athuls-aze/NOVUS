# Deployment Guide

## Architecture
- Backend: Spring Boot 3 on Java 21
- Frontend: React with Vite
- Database: MySQL 8

## Running Locally (Docker Compose)
1. Create a `docker-compose.yml` file.
2. Ensure you have a `.env` file with `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, and `JWT_SECRET`.
3. Build and run: `docker-compose up --build`

## Production Deployment (Cloud)
- **Database**: Use a managed MySQL instance (e.g., AWS RDS).
- **Backend**: Deploy the Docker container to a service like AWS ECS or Heroku.
- **Frontend**: Build the Vite static assets (`npm run build`) and deploy to a CDN/Static Host like Vercel, Netlify, or AWS S3 + CloudFront.

## Environment Variables
- `SPRING_PROFILES_ACTIVE=prod`
- `DB_HOST`, `DB_PORT`, `DB_NAME`
- `JWT_SECRET` (Must be a securely generated 256-bit key)
- `CORS_ALLOWED_ORIGIN`
