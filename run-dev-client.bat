@echo off
setlocal ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION

REM Change to the directory of this script (project root)
cd /d "%~dp0"

REM Launch the Forge dev client at 1920x1080
call gradlew.bat runClient --no-daemon --console=plain --args="--width 1920 --height 1080"

endlocal
