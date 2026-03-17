# Plantastic 🌿

🌿 Plantastic – The app that pampers your plants! 🌱
No more forgetting or overdoing it!
Receive smart reminders, tailored advice, and fun facts.
🍀 With [Plantastic](https://plantastic-nu.vercel.app/), grow your indoor garden 🍀

## Tech stack

- **Frontend** : React / TypeScript, Tailwind CSS — deployed on Vercel
- **Backend** : Spring Boot / Java — deployed on Koyeb
- **Database** : MySQL — hosted on a private server, we used DuckDns, Let's Encrypt and Docker
- **CI/CD** : GitHub Actions

## Prerequisites

- Node.js 22+
- Java 21
- MySQL 8+
- Maven

## Environment variables

### Backend (`backend/src/main/resources/application.properties`)

```
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
FRONTEND_STAGING_URL=
FRONTEND_PROD_URL=
```

### Frontend (`.env`)

```
VITE_API_URL=
```

## Run the project locally

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Run tests

### Unit tests (Vitest)

```bash
cd frontend
npm run test
```

### E2E tests (Playwright)

```bash
cd frontend
npx playwright test
```

## Deployment

- **Frontend** : automatic deployment on Vercel on every push to `main`
- **Backend** : automatic deployment on Koyeb on every push to `main`
- **CI** : GitHub Actions runs Vitest and Playwright tests on every push to `develop` and `main`

## Database server

The database runs on a dedicated server secured with the following measures:

- SSL enabled with a Let's Encrypt certificate (auto-renewed every 90 days via DuckDNS)
- `root` user not exposed to applications
- Dedicated application user with limited privileges (own database only)
- SSL enforced for the application user (`REQUIRE SSL`)

## Security best practices

- Never commit passwords or secrets to Git
- Use environment variables or a vault for all sensitive credentials
