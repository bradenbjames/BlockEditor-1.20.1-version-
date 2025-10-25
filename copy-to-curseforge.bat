@echo off
rem copy-to-curseforge.bat
rem Copies the built be-*.jar from this project's build\libs to the CurseForge instance mods folder.

setlocal enabledelayedexpansion

set "SRC_DIR=%~dp0build\libs"
set "DEST_DIR=C:\Users\brade\curseforge\minecraft\Instances\modTest(1.20.1)\mods"

rem Check source folder
if not exist "%SRC_DIR%" (
  echo Source folder not found: %SRC_DIR%
  pause
  exit /b 1
)

rem Check destination folder
if not exist "%DEST_DIR%" (
  echo Destination folder not found: %DEST_DIR%
  pause
  exit /b 1
)

pushd "%SRC_DIR%"

rem Find the first be-*.jar and copy it. This will overwrite same-named file in destination.
set "FOUND=0"
for %%F in (be-*.jar) do (
  set "FOUND=1"
  echo Found built jar: %%~nxf
  echo Copying "%%~nxf" to "%DEST_DIR%"
  copy /Y "%%~nxf" "%DEST_DIR%\%%~nxf"
  if errorlevel 1 (
    echo Copy failed for %%~nxf
    popd
    pause
    exit /b 1
  ) else (
    echo Copy succeeded: %DEST_DIR%\%%~nxf
  )
  goto :done
)

echo No built be-*.jar found in %SRC_DIR%
popd
pause
exit /b 1

:done
popd
echo Done.
dir "%DEST_DIR%\be-*.jar"
pause
endlocal
