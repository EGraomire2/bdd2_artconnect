param(
    [ValidateSet("compile", "run", "clean", "rebuild")]
    [string]$Action = "compile"
)

$env:PATH += ";C:\Maven\bin"

Write-Host "`n=== ArtConnect Build Script ===" -ForegroundColor Cyan

switch ($Action) {
    "compile" {
        Write-Host "Compilation en cours..." -ForegroundColor Yellow
        mvn clean compile
    }
    "run" {
        Write-Host "Lancement de l'application..." -ForegroundColor Yellow
        mvn clean javafx:run
    }
    "clean" {
        Write-Host "Nettoyage..." -ForegroundColor Yellow
        mvn clean
    }
    "rebuild" {
        Write-Host "Recompilation complète..." -ForegroundColor Yellow
        mvn clean compile
        Write-Host "`nLancement..." -ForegroundColor Yellow
        mvn javafx:run
    }
}

Write-Host "`nTerminé`n" -ForegroundColor Green
