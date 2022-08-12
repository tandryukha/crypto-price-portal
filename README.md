# Getting Started

# Running
- Make sure Docker desktop is installed & running
- Run `docker-compose up`

# Running without docker cache (during development)
1. Stop and remove existing containers `docker-compose down --rmi all`
2. Start and infrastructure locally in docker compose `docker-compose up --force-recreate`

# Development
To package and run tests execute `./mvnw clean package`

# Customization

# Implementation details

# Further improvements