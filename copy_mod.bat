@echo off
REM copy_mod.bat - copies the built mod JAR into the specified CurseForge mods folder
setlocal

:: Usage: copy_mod.bat [SRC] [DST]
:: If SRC or DST are provided as command-line arguments they will override the defaults below.
set "SRC=C:\Users\brade\Desktop\BlockEditor(1.20.1 version)\build\libs\be-1.0.4.jar"
set "DST=C:\Users\brade\curseforge\minecraft\Instances\modTest(1.20.1)\mods"

if not "%~1"=="" set "SRC=%~1"
if not "%~2"=="" set "DST=%~2"

echo Source: "%SRC%"
echo Destination: "%DST%"

:: Ensure destination exists
if not exist "%DST%" (
  mkdir "%DST%" 2>nul
  if errorlevel 1 (
    echo Failed to create destination: "%DST%"
    endlocal & exit /b 3
  )
  echo Created destination: "%DST%"
) else (
  echo Destination exists: "%DST%"
)

:: Ensure source exists
if not exist "%SRC%" (
  echo Source JAR not found: "%SRC%"
  endlocal & exit /b 2
)

:: Extract filename from SRC for verification listing
for %%F in ("%SRC%") do set "FNAME=%%~nxF"

:: Copy the file (suppress normal output). Redirect stderr too.
copy /Y "%SRC%" "%DST%\" >nul 2>&1
if errorlevel 1 (
  echo Copy failed with exit %ERRORLEVEL%
  endlocal & exit /b %ERRORLEVEL%
)

:: Confirm the file was copied
if exist "%DST%\%FNAME%" (
  echo COPIED: "%DST%\%FNAME%"
  dir "%DST%\%FNAME%"
) else (
  echo Copy claimed success but file not found at destination: "%DST%\%FNAME%"
  dir "%DST%"
  endlocal & exit /b 4
)

endlocal
