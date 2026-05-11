@echo off
setlocal enabledelayedexpansion
set "ACTION=%1"
if "%ACTION%"=="" set "ACTION=compile"

set "PATH=C:\Maven\bin;%PATH%"

echo.
echo === ArtConnect Build Script ===
echo.

if /i "%ACTION%"=="compile" (
    echo Compilation en cours...
    call mvn clean compile
) else if /i "%ACTION%"=="run" (
    echo Lancement de l'application...
    call mvn clean javafx:run
) else if /i "%ACTION%"=="clean" (
    echo Nettoyage...
    call mvn clean
) else if /i "%ACTION%"=="rebuild" (
    echo Recompilation complete...
    call mvn clean compile
    echo.
    echo Lancement...
    call mvn javafx:run
) else (
    echo Usage: build.bat [compile^|run^|clean^|rebuild]
    echo   compile : Compile l'application
    echo   run     : Lance l'application
    echo   clean   : Nettoie les fichiers generes
    echo   rebuild : Recompile et lance
)

echo.
echo Termine
echo.
