FROM node:20-alpine
WORKDIR /app
RUN apk add --no-cache ca-certificates
COPY package*.json ./
RUN npm config set strict-ssl false
RUN npm install
COPY . .
EXPOSE 3000
CMD ["npm", "start"]
