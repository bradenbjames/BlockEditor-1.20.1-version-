# Ultimate Quick Script - Build, Copy, Launch
# Usage: .\dev.ps1

Write-Host "🔧 Build → 📦 Copy → 🚀 Launch" -ForegroundColor Magenta

# Build
Write-Host "Building..." -ForegroundColor Yellow -NoNewline
& .\gradlew.bat build -q
if ($LASTEXITCODE -eq 0) { Write-Host " ✅" -ForegroundColor Green } else { Write-Host " ❌" -ForegroundColor Red; exit }

# Copy
Write-Host "Copying..." -ForegroundColor Yellow -NoNewline
Copy-Item "build\libs\be-1.0.2.jar" "C:\Users\$env:USERNAME\curseforge\minecraft\Instances\modTest(1.20.1)\mods\" -Force
Write-Host " ✅" -ForegroundColor Green

# Launch
Write-Host "Launching..." -ForegroundColor Yellow -NoNewline
$cf = @(
    "C:\Users\$env:USERNAME\curseforge\minecraft\Install\CurseForge.exe",
    "C:\Users\$env:USERNAME\AppData\Local\CurseForge\CurseForge.exe"
) | Where-Object { Test-Path $_ } | Select-Object -First 1

if ($cf) {
    try {
        Start-Process $cf -ArgumentList @("--launch", "modTest(1.20.1)") -ErrorAction Stop
        Write-Host " ✅" -ForegroundColor Green
    } catch {
        Start-Process $cf
        Write-Host " ⚠️" -ForegroundColor Yellow
    }
} else {
    Write-Host " ❌" -ForegroundColor Red
}

Write-Host "🎮 Ready!" -ForegroundColor Green