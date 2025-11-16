@echo off
set APP_NAME=mi-playlist
set JAR_FILE=target\mi-playlist-0.0.1-SNAPSHOT.jar

echo === Deploy de %APP_NAME% (Windows) ===

IF NOT EXIST %JAR_FILE% (
    echo ERROR: No se encontro el JAR %JAR_FILE%. ¿Corriste "mvn package"?
    exit /b 1
)

echo Iniciando aplicacion...
REM Esto abre otra ventana con la app corriendo
start "mi-playlist" cmd /c "java -jar %JAR_FILE%"
echo Aplicacion desplegada.
