#!/bin/bash
set -e

APP_NAME="mi-playlist"
JAR_FILE="target/mi-playlist-0.0.1-SNAPSHOT.jar"
PID_FILE="mi-playlist.pid"
LOG_FILE="app.log"

echo "=== Deploy de $APP_NAME (Mac) ==="

if [ ! -f "$JAR_FILE" ]; then
  echo "ERROR: No se encontró el JAR $JAR_FILE. ¿Corriste 'mvn package'?"
  exit 1
fi

# Si hay un PID guardado, intentamos matar el proceso viejo
if [ -f "$PID_FILE" ]; then
  OLD_PID=$(cat "$PID_FILE")
  if kill -0 "$OLD_PID" 2>/dev/null; then
    echo "Deteniendo instancia anterior (PID=$OLD_PID)..."
    kill "$OLD_PID" || true
    sleep 5
  fi
fi

echo "Levantando nueva versión..."
nohup java -jar "$JAR_FILE" > "$LOG_FILE" 2>&1 &

NEW_PID=$!
echo "$NEW_PID" > "$PID_FILE"

echo "Aplicación desplegada con PID=$NEW_PID"
echo "Logs en $LOG_FILE"
