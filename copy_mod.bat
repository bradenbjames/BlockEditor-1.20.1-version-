@echo off
REM copy_mod.bat - copies the built mod JAR into the specified CurseForge mods folder
setlocal
set "SRC=C:\Users\brade\Desktop\BlockEditor(1.20.1 version)\build\libs\be-1.0.4.jar"
set "DST=C:\Users\brade\curseforge\minecraft\Instances\modTest(1.20.1)\mods"

n
nif not exist "%DST%" (
  mkdir "%DST%"
  echo Created destination: %DST%
) else (
  echo Destination exists: %DST%
)

nif not exist "%SRC%" (
  echo Source JAR not found: %SRC%
  exit /b 2
)

ncopy /Y "%SRC%" "%DST%\" >nul
nif %ERRORLEVEL% neq 0 (
  echo Copy failed with exit %ERRORLEVEL%
  exit /b %ERRORLEVEL%
)
echo COPIED
dir "%DST%\be-1.0.4.jar"
endlocal

