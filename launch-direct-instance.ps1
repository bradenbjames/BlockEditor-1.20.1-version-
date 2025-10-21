# Direct Minecraft Instance Launcher
# Launches the specific modTest(1.20.1) instance directly

param(
    [switch]$Debug
)

$ErrorActionPreference = "Stop"

Write-Host "🚀 Direct Minecraft Instance Launcher" -ForegroundColor Magenta
Write-Host "=====================================" -ForegroundColor Magenta
Write-Host ""

# Instance configuration
$instancePath = "C:\Users\$env:USERNAME\curseforge\minecraft\Instances\modTest(1.20.1)"
$minecraftVersion = "1.20.1"

Write-Host "🎯 Target Instance: modTest(1.20.1)" -ForegroundColor Cyan
Write-Host "📁 Instance Path: $instancePath" -ForegroundColor Gray

# Check if instance exists
if (-not (Test-Path $instancePath)) {
    Write-Host "❌ Instance not found at: $instancePath" -ForegroundColor Red
    Write-Host "Please verify the path is correct." -ForegroundColor Red
    exit 1
}

Write-Host "✅ Instance found!" -ForegroundColor Green

# Look for common CurseForge launcher patterns
$possibleLaunchers = @(
    "C:\Users\$env:USERNAME\curseforge\minecraft\Install\CurseForge.exe",
    "C:\Users\$env:USERNAME\AppData\Local\CurseForge\CurseForge.exe",
    "C:\Program Files (x86)\CurseForge\CurseForge.exe",
    "C:\Program Files\CurseForge\CurseForge.exe"
)

# Method 1: Try to use CurseForge with instance-specific arguments
Write-Host ""
Write-Host "🔍 Searching for CurseForge launcher..." -ForegroundColor Yellow

$curseForgeExe = $null
foreach ($launcher in $possibleLaunchers) {
    if (Test-Path $launcher) {
        $curseForgeExe = $launcher
        Write-Host "✅ Found CurseForge at: $launcher" -ForegroundColor Green
        break
    }
}

if ($curseForgeExe) {
    Write-Host ""
    Write-Host "🚀 Attempting to launch instance..." -ForegroundColor Yellow
    
    # Try different CurseForge command line approaches
    $launchAttempts = @(
        # Method 1: Direct instance path
        @("--instance", "`"$instancePath`""),
        # Method 2: Instance name
        @("--launch", "modTest(1.20.1)"),
        # Method 3: Instance ID approach
        @("--profile", "modTest(1.20.1)")
    )
    
    $launched = $false
    foreach ($attempt in $launchAttempts) {
        try {
            if ($Debug) {
                Write-Host "Debug: Trying command: $curseForgeExe $($attempt -join ' ')" -ForegroundColor Gray
            }
            
            Start-Process -FilePath $curseForgeExe -ArgumentList $attempt -ErrorAction Stop
            $launched = $true
            Write-Host "✅ Launch command sent!" -ForegroundColor Green
            break
        } catch {
            if ($Debug) {
                Write-Host "Debug: Attempt failed: $($_.Exception.Message)" -ForegroundColor Gray
            }
            continue
        }
    }
    
    if (-not $launched) {
        Write-Host "⚠️ Direct launch failed, opening CurseForge normally..." -ForegroundColor Yellow
        Start-Process -FilePath $curseForgeExe
    }
} else {
    Write-Host "❌ CurseForge not found!" -ForegroundColor Red
}

# Method 2: Alternative - Try to launch via Minecraft launcher if available
Write-Host ""
Write-Host "🔄 Alternative: Checking for Minecraft Launcher..." -ForegroundColor Yellow

$minecraftLauncher = "C:\Users\$env:USERNAME\AppData\Local\Packages\Microsoft.4297127D64EC6_8wekyb3d8bbwe\LocalCache\Local\game\Minecraft Launcher\MinecraftLauncher.exe"
$minecraftLauncherAlt = "C:\Program Files (x86)\Minecraft Launcher\MinecraftLauncher.exe"

$mcLauncher = $null
if (Test-Path $minecraftLauncher) {
    $mcLauncher = $minecraftLauncher
} elseif (Test-Path $minecraftLauncherAlt) {
    $mcLauncher = $minecraftLauncherAlt
}

if ($mcLauncher) {
    Write-Host "✅ Found Minecraft Launcher at: $mcLauncher" -ForegroundColor Green
    Write-Host "💡 You could also try launching through the official launcher" -ForegroundColor Cyan
}

Write-Host ""
Write-Host "📋 Instructions:" -ForegroundColor Cyan
Write-Host "   1. CurseForge should be opening" -ForegroundColor White
Write-Host "   2. If it opens to the main screen, navigate to 'My Modpacks'" -ForegroundColor White
Write-Host "   3. Find 'modTest(1.20.1)' and click 'Play'" -ForegroundColor White
Write-Host ""
Write-Host "💡 Your BlockEditor mod (be-1.0.2.jar) is already installed!" -ForegroundColor Green

Write-Host ""
Write-Host "🔧 Troubleshooting:" -ForegroundColor Yellow
Write-Host "   If direct launch doesn't work, try:" -ForegroundColor White
Write-Host "   - .\launch-direct-instance.ps1 -Debug  (for detailed output)" -ForegroundColor White
Write-Host "   - Use the regular .\launch-minecraft.ps1 script" -ForegroundColor White

Write-Host ""
Write-Host "Press any key to continue..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")