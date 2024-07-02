docker compose up --build -d

javac -d target/classes src/main/java/com/tzuchaedahy/**/*.java

java -cp target/classes com.tzuchaedahy.application.Main

docker compose down