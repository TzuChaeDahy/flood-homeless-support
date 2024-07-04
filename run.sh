docker compose up --build -d

mvn clean package

java -jar ./target/flood-homeless-support-1.0-SNAPSHOT.jar

docker compose down