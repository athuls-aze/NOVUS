# NOVUS API Documentation

## Base URL
`/api/v1`

## Global Error Format
All errors return a consistent JSON envelope:
```json
{
  "timestamp": "2026-09-27T10:00:00",
  "status": 400,
  "error": "Error reason or Validation Failed",
  "details": {
     "field": "Validation message"
  }
}
```

## Endpoints

### Auth
- `POST /auth/register` - Register a new user
- `POST /auth/login` - Authenticate and receive a JWT

### User Devices
- `GET /user-devices` - List devices owned by the authenticated user (Supports Pagination: `?page=0&size=10`)
- `POST /user-devices` - Register a new device
- `GET /user-devices/{id}` - Get details of a specific device

### Warranty
- `GET /user-devices/{deviceId}/warranty` - Get warranty status
- `POST /user-devices/{deviceId}/warranty` - Create or update warranty

### Condition Assessment
- `GET /user-devices/{deviceId}/condition` - Get latest assessment
- `POST /user-devices/{deviceId}/condition` - Submit new condition assessment

### Market Data
- `GET /market-data/{variantId}` - Public endpoint to retrieve 6 months of market data history

### Valuation
- `GET /user-devices/{deviceId}/valuation` - Get latest valuation
- `POST /user-devices/{deviceId}/valuation` - Calculate and save a new valuation

### Smart Assistant
- `POST /user-devices/{deviceId}/assistant/query` - Ask the smart assistant a question about the device
