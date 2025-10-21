# Launch Minecraft CurseForge Instance Script
# This script launches your modTest(1.20.1) instance

Write-Host "🎮 Launching Minecraft CurseForge Instance: modTest(1.20.1)" -ForegroundColor Green

# Path to CurseForge executable
$curseForgeExe = "C:\Users\$env:USERNAME\curseforge\minecraft\Install\CurseForge.exe"

# Alternative common paths for CurseForge
$alternatePaths = @(
    "C:\Users\$env:USERNAME\AppData\Local\CurseForge\CurseForge.exe",
    "C:\Program Files (x86)\CurseForge\CurseForge.exe",
    "C:\Program Files\CurseForge\CurseForge.exe"
)

# Find CurseForge executable
$foundPath = $null
if (Test-Path $curseForgeExe) {
    $foundPath = $curseForgeExe
} else {
    foreach ($path in $alternatePaths) {
        if (Test-Path $path) {
            $foundPath = $path
            break
        }
    }
}

if ($foundPath) {
    Write-Host "✅ Found CurseForge at: $foundPath" -ForegroundColor Green
    
    # Launch CurseForge with the specific instance
    # Note: CurseForge doesn't have direct command line arguments to launch specific instances
    # So we'll just open CurseForge and you can manually select the instance
    
    Write-Host "🚀 Starting CurseForge..." -ForegroundColor Yellow
    Start-Process -FilePath $foundPath
    
    Write-Host ""
    Write-Host "📋 Instructions:" -ForegroundColor Cyan
    Write-Host "   1. CurseForge should now be opening" -ForegroundColor White
    Write-Host "   2. Navigate to 'My Modpacks'" -ForegroundColor White  
    Write-Host "   3. Find 'modTest(1.20.1)' and click 'Play'" -ForegroundColor White
    Write-Host ""
    Write-Host "💡 Your mod 'be-1.0.2.jar' is already installed!" -ForegroundColor Green
    
} else {
    Write-Host "❌ CurseForge not found!" -ForegroundColor Red
    Write-Host "Please install CurseForge or update the path in this script." -ForegroundColor Red
    Write-Host ""
    Write-Host "Common installation locations:" -ForegroundColor Yellow
    Write-Host "  - C:\Users\$env:USERNAME\curseforge\minecraft\Install\CurseForge.exe" -ForegroundColor White
    Write-Host "  - C:\Users\$env:USERNAME\AppData\Local\CurseForge\CurseForge.exe" -ForegroundColor White
    Write-Host "  - C:\Program Files (x86)\CurseForge\CurseForge.exe" -ForegroundColor White
}

Write-Host ""
Write-Host "Press any key to continue..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")