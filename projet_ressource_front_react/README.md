-- ce projet contient : 
- la partie front (visual studio)

- pré-requis : installer node.js, et expo

avant de faire tourner le projet pour la 1ere fois:
- ouvrir un gitbash sur le dossier du front 
- taper npx create-expo-app@latest
- npm install @react-native-picker/picker -> pour la combo box 
- npm install react-native-multiple-select -> pour la multiple selection type relation
- vérifier s'il ne faut pas faire d'autres install de dépendances je ne sais pas si je les ai toutes noté
- ensuite lancer l'api avec intelij
- puis taper la commande "npm run web" dans la fenetre bash du dossier front

## Déploiement avec Docker et Docker Compose

Prérequis: Docker Desktop 4.x+

1. Copiez `.env.docker.example` en `.env` et adaptez les valeurs.
2. Lancez la stack:

```bash
docker compose up -d --build
```

3. Accès:
- Frontend (Expo web): `http://localhost:${FRONTEND_PORT}` (par défaut 8080)
- Backend (Spring Boot): `http://localhost:${BACKEND_PORT}` (par défaut 8080)
- PostgreSQL: `localhost:${POSTGRES_PORT}` (par défaut 5432)

Variables utiles (.env):
- POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD, POSTGRES_PORT
- BACKEND_IMAGE, BACKEND_PORT
- FRONTEND_PORT, BACKEND_URL (utilisé pour `EXPO_PUBLIC_API_URL`)

Notes:
- Le frontend lit `EXPO_PUBLIC_API_URL` au build/au runtime pour pointer vers le backend.
- En prod, préférez une image de backend publiée (renseignez `BACKEND_IMAGE`).
