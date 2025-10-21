# Complete Build & Launch Script
# This script builds the mod, copies it to CurseForge, and launches Minecraft

param(
    [switch]$SkipBuild,
    [switch]$SkipCopy,
    [switch]$SkipLaunch
)

$ErrorActionPreference = "Stop"

Write-Host "🔧 BlockEditor Mod - Build & Launch Script" -ForegroundColor Magenta
Write-Host "==========================================" -ForegroundColor Magenta
Write-Host ""

# Set paths
$projectPath = Get-Location
$buildOutputPath = Join-Path $projectPath "build\libs\be-1.0.2.jar"
$curseForgeModsPath = "C:\Users\$env:USERNAME\curseforge\minecraft\Instances\modTest(1.20.1)\mods"

# Step 1: Build the mod
if (-not $SkipBuild) {
    Write-Host "🔨 Building mod..." -ForegroundColor Yellow
    try {
        & .\gradlew.bat build
        Write-Host "✅ Build successful!" -ForegroundColor Green
    } catch {
        Write-Host "❌ Build failed!" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "⏭️ Skipping build (--SkipBuild specified)" -ForegroundColor Gray
}

# Step 2: Copy to CurseForge
if (-not $SkipCopy) {
    Write-Host ""
    Write-Host "📂 Copying mod to CurseForge..." -ForegroundColor Yellow
    
    if (Test-Path $buildOutputPath) {
        if (Test-Path $curseForgeModsPath) {
            try {
                Copy-Item $buildOutputPath $curseForgeModsPath -Force
                Write-Host "✅ Mod copied successfully!" -ForegroundColor Green
            } catch {
                Write-Host "❌ Failed to copy mod!" -ForegroundColor Red
                Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
                exit 1
            }
        } else {
            Write-Host "❌ CurseForge mods directory not found: $curseForgeModsPath" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "❌ Built mod not found: $buildOutputPath" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "⏭️ Skipping copy (--SkipCopy specified)" -ForegroundColor Gray
}

# Step 3: Launch Minecraft
if (-not $SkipLaunch) {
    Write-Host ""
    Write-Host "🎮 Launching Minecraft..." -ForegroundColor Yellow
    
    # Find CurseForge executable
    $curseForgeExe = "C:\Users\$env:USERNAME\curseforge\minecraft\Install\CurseForge.exe"
    $alternatePaths = @(
        "C:\Users\$env:USERNAME\AppData\Local\CurseForge\CurseForge.exe",
        "C:\Program Files (x86)\CurseForge\CurseForge.exe",
        "C:\Program Files\CurseForge\CurseForge.exe"
    )
    
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
        Start-Process -FilePath $foundPath
        Write-Host "🚀 CurseForge launched!" -ForegroundColor Green
    } else {
        Write-Host "❌ CurseForge not found! Please launch manually." -ForegroundColor Red
    }
} else {
    Write-Host "⏭️ Skipping launch (--SkipLaunch specified)" -ForegroundColor Gray
}

Write-Host ""
Write-Host "🎉 All done!" -ForegroundColor Green
Write-Host ""
Write-Host "📋 Next steps:" -ForegroundColor Cyan
Write-Host "   1. In CurseForge, go to 'My Modpacks'" -ForegroundColor White
Write-Host "   2. Find 'modTest(1.20.1)' and click 'Play'" -ForegroundColor White
Write-Host "   3. Test your BlockEditor mod with middle-click functionality!" -ForegroundColor White
Write-Host ""
Write-Host "💡 Usage examples:" -ForegroundColor Yellow
Write-Host "   .\build-and-launch.ps1                 # Full build, copy, and launch" -ForegroundColor White
Write-Host "   .\build-and-launch.ps1 -SkipBuild      # Only copy and launch" -ForegroundColor White
Write-Host "   .\build-and-launch.ps1 -SkipLaunch     # Only build and copy" -ForegroundColor White