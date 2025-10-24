@echo off
setlocal ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION

REM Change to the directory of this script (project root)
cd /d "%~dp0"

REM Launch the Forge dev client
call gradlew.bat runClient --no-daemon --console=plain

endlocal

