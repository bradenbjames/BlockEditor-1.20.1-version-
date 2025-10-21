# Advanced Direct Minecraft Launcher
# Attempts to launch Minecraft directly using instance configuration

param(
    [switch]$Debug,
    [switch]$ForceRebuild
)

$ErrorActionPreference = "Continue"  # Continue on errors for better troubleshooting

Write-Host "🚀 Advanced Minecraft Direct Launcher" -ForegroundColor Magenta
Write-Host "=====================================" -ForegroundColor Magenta
Write-Host ""

# Configuration
$instanceName = "modTest(1.20.1)"
$instancePath = "C:\Users\$env:USERNAME\curseforge\minecraft\Instances\$instanceName"
$username = $env:USERNAME

Write-Host "🎯 Target: $instanceName" -ForegroundColor Cyan
Write-Host "📁 Path: $instancePath" -ForegroundColor Gray
Write-Host ""

# Function to find Java installation
function Find-JavaInstallation {
    $javaPaths = @(
        "C:\Program Files\Java\jdk-17*\bin\java.exe",
        "C:\Program Files\Java\jre*\bin\java.exe", 
        "C:\Program Files (x86)\Java\jre*\bin\java.exe",
        "C:\Users\$username\.jdks\*\bin\java.exe",
        "$env:JAVA_HOME\bin\java.exe"
    )
    
    foreach ($pattern in $javaPaths) {
        $found = Get-ChildItem -Path (Split-Path $pattern) -Filter (Split-Path $pattern -Leaf) -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($found) {
            return $found.FullName
        }
    }
    
    # Try PATH
    try {
        $javaCheck = & java -version 2>&1
        if ($LASTEXITCODE -eq 0) {
            return "java"
        }
    } catch {}
    
    return $null
}

# Function to get Minecraft authentication
function Get-MinecraftAuth {
    # This is simplified - in reality you'd need proper authentication
    Write-Host "⚠️ Note: This script uses offline mode authentication" -ForegroundColor Yellow
    return @{
        Username = $username
        UUID = "00000000-0000-0000-0000-000000000000"
        AccessToken = "offline"
        UserType = "legacy"
    }
}

# Check if instance exists
if (-not (Test-Path $instancePath)) {
    Write-Host "❌ Instance not found: $instancePath" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Instance found!" -ForegroundColor Green

# Find Java
Write-Host "🔍 Searching for Java..." -ForegroundColor Yellow
$javaExe = Find-JavaInstallation
if (-not $javaExe) {
    Write-Host "❌ Java not found! Please install Java 17 or later." -ForegroundColor Red
    Write-Host "💡 Download from: https://adoptium.net/" -ForegroundColor Cyan
    exit 1
}
Write-Host "✅ Java found: $javaExe" -ForegroundColor Green

# Try to read instance configuration
$instanceConfig = Join-Path $instancePath "minecraftinstance.json"
$modsPath = Join-Path $instancePath "mods"

if (Test-Path $instanceConfig) {
    Write-Host "✅ Instance config found" -ForegroundColor Green
    try {
        $config = Get-Content $instanceConfig | ConvertFrom-Json
        if ($Debug) {
            Write-Host "Debug: Instance config loaded" -ForegroundColor Gray
        }
    } catch {
        Write-Host "⚠️ Could not parse instance config" -ForegroundColor Yellow
    }
}

# Check for mods
if (Test-Path $modsPath) {
    $modCount = (Get-ChildItem $modsPath -Filter "*.jar").Count
    Write-Host "✅ Mods folder found ($modCount mods)" -ForegroundColor Green
    
    # Check for our mod specifically
    $ourMod = Join-Path $modsPath "be-1.0.2.jar"
    if (Test-Path $ourMod) {
        Write-Host "✅ BlockEditor mod found!" -ForegroundColor Green
    }
}

# Attempt multiple launch strategies
Write-Host ""
Write-Host "🚀 Attempting to launch..." -ForegroundColor Yellow

# Strategy 1: Use CurseForge with potential command line args
$curseForgeExe = @(
    "C:\Users\$username\curseforge\minecraft\Install\CurseForge.exe",
    "C:\Users\$username\AppData\Local\CurseForge\CurseForge.exe"
) | Where-Object { Test-Path $_ } | Select-Object -First 1

if ($curseForgeExe) {
    Write-Host "Strategy 1: CurseForge direct launch..." -ForegroundColor Cyan
    
    # Try various CurseForge command line arguments
    $cfArgs = @(
        @("--instance-path", "`"$instancePath`""),
        @("--launch", "`"$instanceName`""),
        @("--profile", "`"$instanceName`""),
        @("--game-dir", "`"$instancePath`"")
    )
    
    foreach ($args in $cfArgs) {
        try {
            if ($Debug) {
                Write-Host "  Trying: $curseForgeExe $($args -join ' ')" -ForegroundColor Gray
            }
            Start-Process -FilePath $curseForgeExe -ArgumentList $args -NoNewWindow:$false
            Start-Sleep -Seconds 2
            
            # Check if Minecraft process started
            Start-Sleep -Seconds 5
            $mcProcess = Get-Process | Where-Object { $_.ProcessName -like "*minecraft*" -or $_.ProcessName -like "*java*" -and $_.MainWindowTitle -like "*Minecraft*" }
            if ($mcProcess) {
                Write-Host "✅ Minecraft appears to be launching!" -ForegroundColor Green
                Write-Host "🎮 Game should start shortly..." -ForegroundColor Green
                exit 0
            }
        } catch {
            if ($Debug) {
                Write-Host "  Failed: $($_.Exception.Message)" -ForegroundColor Gray
            }
        }
    }
}

# Strategy 2: Direct Minecraft launch (simplified)
Write-Host "Strategy 2: Fallback to CurseForge launcher..." -ForegroundColor Cyan

if ($curseForgeExe) {
    Start-Process -FilePath $curseForgeExe
    Write-Host "✅ CurseForge opened!" -ForegroundColor Green
    
    Write-Host ""
    Write-Host "📋 Manual Steps:" -ForegroundColor Yellow
    Write-Host "   1. In CurseForge, click 'My Modpacks'" -ForegroundColor White
    Write-Host "   2. Find '$instanceName'" -ForegroundColor White
    Write-Host "   3. Click 'Play'" -ForegroundColor White
} else {
    Write-Host "❌ Could not find CurseForge launcher!" -ForegroundColor Red
    Write-Host "Please launch manually from: $instancePath" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "💡 Tips:" -ForegroundColor Cyan
Write-Host "   - Your BlockEditor mod is ready to test!" -ForegroundColor White
Write-Host "   - Try middle-clicking blocks in the Recent Blocks panel" -ForegroundColor White
Write-Host "   - Check console output for debugging info" -ForegroundColor White