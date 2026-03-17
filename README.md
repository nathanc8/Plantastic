# Plantastic
 
Plantastic is a web application for managing houseplants, allowing users to track watering, manage their personal garden, and browse a botanical encyclopedia.
 
The application is available at:
https://plantastic-nu.vercel.app/
 
## About
 
Plantastic lets users build a personalized digital garden, add their plants, track watering schedules, and discover new species through a botanical encyclopedia. The goal is to provide a simple, visual tool to help users take care of their plants on a daily basis.
 
This project was developed as part of a French RNCP professional certification, as a full-stack end-to-end project.
 
## Features
 
- User registration and session-based authentication
- Personal garden with watering tracking
- Per-plant thirst indicator
- Custom photo upload for each plant
- Browsable botanical encyclopedia
- Responsive interface
 
## Tech Stack
 
**Frontend**
- React 19 + TypeScript
- Vite
- Tailwind CSS
- Zod (form validation)
 
**Backend**
- Java 21
- Spring Boot 3
- MySQL 8
 
**External Services**
- Cloudinary (photo upload and storage)
- Perenual API (botanical data) : https://perenual.com/
 
**Deployment**
- Frontend: Vercel
- Backend: Koyeb
- Database: MySQL on a private server (Docker, DuckDNS, Let's Encrypt)
 
## Development
 
### Requirements
 
- Node.js (recent version recommended)
- npm
- Java 21
- Maven
 
### Frontend setup
 
```bash
git clone https://github.com/<your-username>/plantastic.git
cd plantastic/frontend
npm install
```
 
### Run in development mode
 
```bash
npm run dev
```
 
### Run tests
 
```bash
npm run test
```
 
### Production build
 
```bash
npm run build
```
 
### Backend setup
 
```bash
cd plantastic/backend
```
 
Create an `application-local.properties` file with the required variables (see `application-exemple.properties` for the expected variable names).
 
```bash
mvn spring-boot:run
```
 
## Environment Variables
 
The backend uses environment variables for all sensitive configuration. No secrets are committed to the repository.
 
| Variable | Description |
|---|---|
| `DB_HOST` | Database host |
| `DB_PORT` | Database port |
| `DB_NAME` | Database name |
| `DB_USERNAME` | MySQL user |
| `DB_PASSWORD` | MySQL password |
| `API_KEY` | Perenual API key |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret |
| `FRONTEND_PROD_URL` | Production frontend URL |
| `FRONTEND_STAGING_URL` | Staging frontend URL |
 
## Author
 
Developed by Yennie Lake and Nathan Cazard as part of a professional certification project.
