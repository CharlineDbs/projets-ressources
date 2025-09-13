FROM node:20
WORKDIR /app
RUN npm install -g qrcode-terminal
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 8081
CMD ["sh", "-c", "npx expo start --lan"]
