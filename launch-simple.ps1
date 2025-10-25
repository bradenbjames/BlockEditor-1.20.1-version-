# launch-simple.ps1
<#
Simple launcher script for development: runs the Gradle wrapper build (unless -SkipBuild) and then runs runClient
with default window size 1920x1080. Forwards additional Gradle args if provided.

Usage examples:
  ./launch-simple.ps1                # build then runClient at 1920x1080
  ./launch-simple.ps1 -SkipBuild     # skip the build, just runClient
  ./launch-simple.ps1 -Width 1280 -Height 720
  ./launch-simple.ps1 -GradleArgs "--no-daemon" "-DsomeProp=val"
#>

param(
    [switch]$SkipBuild,
    [int]$Width = 1920,
    [int]$Height = 1080,
    [string[]]$GradleArgs = @()
)

# Change to script directory (project root)
$ScriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
Push-Location $ScriptRoot

# Find Gradle wrapper
if (Test-Path .\gradlew.bat) {
    $wrapper = '.\gradlew.bat'
} elseif (Test-Path .\gradlew) {
    $wrapper = '.\gradlew'
} else {
    Write-Error "Gradle wrapper not found in project root. Expected gradlew.bat or gradlew"
    Pop-Location
    exit 1
}

# Build step (optional)
if (-not $SkipBuild) {
    Write-Host "Running: $wrapper build"
    & $wrapper build
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Build failed with exit code $LASTEXITCODE. Aborting runClient."
        Pop-Location
        exit $LASTEXITCODE
    }
}

# Construct --args string for runClient (pass width/height and any extra args)
$extra = if ($GradleArgs -and $GradleArgs.Length -gt 0) { ($GradleArgs -join ' ') } else { '' }
$argsString = "--width $Width --height $Height $extra".Trim()
$gradleArg = "--args=$argsString"

Write-Host "Running: $wrapper runClient $gradleArg"
& $wrapper runClient $gradleArg
$exitCode = $LASTEXITCODE

Pop-Location
exit $exitCode
