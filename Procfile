web: java $JAVA_OPTS -Dspring.profiles.active=heroku -Dserver.port=$PORT -jar secretPal-backend/target/*.jar
migrate: java $JAVA_OPTS -cp secretPal-migrator/target/*.jar com.tenPines.SecretPalMigrator
