#!/usr/bin/env sh

PROFILE="dev"
DATABASE_NAME="bojia-tg-bot"
DATABASE_URL="jdbc:postgresql://localhost:5432/$DATABASE_NAME"
DATABASE_USERNAME="postgres"
DATABASE_PASSWORD="postgres"

set -- \
  --spring.profiles.active="$PROFILE" \
  --spring.datasource.url="$DATABASE_URL" \
  --spring.datasource.username="$DATABASE_USERNAME" \
  --spring.datasource.password="$DATABASE_PASSWORD"

echo "./mvnw spring-boot:run -Dspring-boot.run.arguments=\"$*\""

./mvnw spring-boot:run -Dspring-boot.run.arguments="$*"
