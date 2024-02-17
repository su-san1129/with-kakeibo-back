up:
	cd db && docker-compose up -d
db-setup:
	sbt flywayMigrate
