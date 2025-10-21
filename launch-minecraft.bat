@echo off
REM Launch Minecraft CurseForge Instance Batch Script
REM Alternative batch script version

echo.
echo 🎮 Launching Minecraft CurseForge Instance: modTest(1.20.1)
echo.

REM Check for CurseForge installation
set "CURSEFORGE_PATH=C:\Users\%USERNAME%\curseforge\minecraft\Install\CurseForge.exe"
set "CURSEFORGE_ALT1=C:\Users\%USERNAME%\AppData\Local\CurseForge\CurseForge.exe"
set "CURSEFORGE_ALT2=C:\Program Files (x86)\CurseForge\CurseForge.exe"

if exist "%CURSEFORGE_PATH%" (
    echo ✅ Found CurseForge at: %CURSEFORGE_PATH%
    echo 🚀 Starting CurseForge...
    start "" "%CURSEFORGE_PATH%"
    goto :instructions
)

if exist "%CURSEFORGE_ALT1%" (
    echo ✅ Found CurseForge at: %CURSEFORGE_ALT1%
    echo 🚀 Starting CurseForge...
    start "" "%CURSEFORGE_ALT1%"
    goto :instructions
)

if exist "%CURSEFORGE_ALT2%" (
    echo ✅ Found CurseForge at: %CURSEFORGE_ALT2%
    echo 🚀 Starting CurseForge...
    start "" "%CURSEFORGE_ALT2%"
    goto :instructions
)

echo ❌ CurseForge not found!
echo Please install CurseForge or update the path in this script.
echo.
echo Common installation locations:
echo   - C:\Users\%USERNAME%\curseforge\minecraft\Install\CurseForge.exe
echo   - C:\Users\%USERNAME%\AppData\Local\CurseForge\CurseForge.exe
echo   - C:\Program Files (x86)\CurseForge\CurseForge.exe
goto :end

:instructions
echo.
echo 📋 Instructions:
echo    1. CurseForge should now be opening
echo    2. Navigate to 'My Modpacks'
echo    3. Find 'modTest(1.20.1)' and click 'Play'
echo.
echo 💡 Your mod 'be-1.0.2.jar' is already installed!
echo.

:end
echo.
echo Press any key to continue...
pause >nul