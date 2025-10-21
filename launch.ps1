# Quick Minecraft Launcher
# Usage: .\launch.ps1 -sci -a (build, copy, launch all-in-one)

param(
    [switch]$sci,  # Specific Custom Instance
    [switch]$a,    # All (build + copy + launch)
    [switch]$h     # Help
)

if ($h) {
    Write-Host "Quick Minecraft Launcher" -ForegroundColor Cyan
    Write-Host "Usage:" -ForegroundColor Yellow
    Write-Host "  .\launch.ps1 -sci         Launch modTest instance" -ForegroundColor White
    Write-Host "  .\launch.ps1 -sci -a      Build + Copy + Launch (complete workflow)" -ForegroundColor White
    Write-Host "  .\launch.ps1 -h           Show this help" -ForegroundColor White
    exit 0
}

if ($sci) {
    # Build and copy if -a flag is used
    if ($a) {
        Write-Host "Building mod..." -ForegroundColor Yellow
        & .\gradlew.bat build -q
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Build failed!" -ForegroundColor Red
            exit 1
        }
        Write-Host "Build complete!" -ForegroundColor Green
        
        Write-Host "Copying mod..." -ForegroundColor Yellow
        try {
            Copy-Item "build\libs\be-1.0.2.jar" "C:\Users\$env:USERNAME\curseforge\minecraft\Instances\modTest(1.20.1)\mods\" -Force -ErrorAction Stop
            Write-Host "Copy complete!" -ForegroundColor Green
        } catch {
            Write-Host "Copy failed!" -ForegroundColor Red
            exit 1
        }
    }
    
    Write-Host "Launching modTest(1.20.1)..." -ForegroundColor Green
    
    # Find CurseForge
    $cf = @(
        "C:\Users\$env:USERNAME\AppData\Local\Programs\CurseForge Windows\CurseForge.exe",
        "C:\Users\$env:USERNAME\curseforge\minecraft\Install\CurseForge.exe",
        "C:\Users\$env:USERNAME\AppData\Local\CurseForge\CurseForge.exe"
    ) | Where-Object { Test-Path $_ } | Select-Object -First 1
    
        Write-Host "Launching modTest(1.20.1)..." -ForegroundColor Cyan
    
    # Try direct command line launch first (more reliable)
    $instancePath = "C:\Users\brade\curseforge\minecraft\Instances\modTest(1.20.1)"
    if (Test-Path $instancePath) {
        Write-Host "Found instance directory, attempting direct launch..." -ForegroundColor Green
        try {
            # Try to launch directly via CurseForge command line if available
            $cfArgs = "--launch-instance `"$instancePath`""
            Start-Process $cf -ArgumentList $cfArgs -ErrorAction SilentlyContinue
            Start-Sleep -Seconds 3
            
            # Check if Minecraft started
            $minecraft = Get-Process | Where-Object { $_.ProcessName -like "*java*" } 
            if ($minecraft) {
                Write-Host "Direct launch successful - Minecraft is starting!" -ForegroundColor Green
                return
            }
        } catch {
            Write-Host "Direct launch failed, trying UI automation..." -ForegroundColor Yellow
        }
    }
    
    # Fallback to UI automation
    if (Test-Path $cf) {
        Write-Host "Starting CurseForge..." -ForegroundColor Green
        Start-Process $cf
        
        Write-Host "Waiting for CurseForge to load..." -ForegroundColor Yellow
        Start-Sleep -Seconds 3
        
        # Try to bring CurseForge to front and launch the specific instance
        try {
            # Give CurseForge time to fully load
            Start-Sleep -Seconds 4
            
            Write-Host "Looking for CurseForge window..." -ForegroundColor Cyan
            
            # Try to find CurseForge window with better detection
            $curseForgeProcess = $null
            $attempts = 0
            while ($attempts -lt 8 -and $curseForgeProcess -eq $null) {
                $curseForgeProcess = Get-Process | Where-Object { 
                    $_.ProcessName -eq "CurseForge" -and $_.MainWindowHandle -ne 0 
                } | Select-Object -First 1
                
                if ($curseForgeProcess -eq $null) {
                    Start-Sleep -Seconds 1
                    $attempts++
                    Write-Host "Waiting for CurseForge window... ($attempts/8)" -ForegroundColor Yellow
                }
            }
            
            if ($curseForgeProcess) {
                Write-Host "Found CurseForge window, attempting automation..." -ForegroundColor Green
                
                # Load required assemblies for automation
                Add-Type -AssemblyName System.Windows.Forms
                Add-Type -AssemblyName Microsoft.VisualBasic
                
                # Bring window to front
                [Microsoft.VisualBasic.Interaction]::AppActivate($curseForgeProcess.Id)
                Start-Sleep -Seconds 1
                
                Write-Host "Navigating to My Modpacks..." -ForegroundColor Cyan
                
                # Try multiple navigation methods
                # Method 1: Keyboard shortcut for My Modpacks
                [System.Windows.Forms.SendKeys]::SendWait("^2")
                Start-Sleep -Seconds 2
                
                Write-Host "Looking for modTest(1.20.1) instance..." -ForegroundColor Cyan
                
                # Method 2: Try to search for the instance
                # Some apps use Ctrl+F for search
                [System.Windows.Forms.SendKeys]::SendWait("^f")
                Start-Sleep -Seconds 1
                [System.Windows.Forms.SendKeys]::SendWait("modTest")
                Start-Sleep -Seconds 1
                [System.Windows.Forms.SendKeys]::SendWait("{ESC}")
                Start-Sleep -Seconds 1
                
                # Method 3: Navigate through instances with arrow keys
                Write-Host "Attempting to find and launch instance..." -ForegroundColor Cyan
                for ($i = 0; $i -lt 5; $i++) {
                    [System.Windows.Forms.SendKeys]::SendWait("{DOWN}")
                    Start-Sleep -Seconds 0.3
                }
                
                # Try pressing Enter or Space to launch
                [System.Windows.Forms.SendKeys]::SendWait("{ENTER}")
                Start-Sleep -Seconds 2
                
                # Try multiple approaches to click Play
                [System.Windows.Forms.SendKeys]::SendWait("{SPACE}")  # Try space bar
                Start-Sleep -Seconds 1
                [System.Windows.Forms.SendKeys]::SendWait("{ENTER}")  # Try Enter again
                Start-Sleep -Seconds 1
                
                # Try Tab to Play button and Enter
                [System.Windows.Forms.SendKeys]::SendWait("{TAB}{TAB}{ENTER}")
                Start-Sleep -Seconds 1
                
                # Try right-click context menu
                [System.Windows.Forms.SendKeys]::SendWait("+{F10}")  # Shift+F10 for context menu
                Start-Sleep -Seconds 1
                [System.Windows.Forms.SendKeys]::SendWait("p")  # Press 'p' for Play
                
                Write-Host "Launch sequence completed. Check if Minecraft is starting..." -ForegroundColor Green
                
                # Wait a moment and check for Minecraft process
                Start-Sleep -Seconds 3
                $minecraft = Get-Process | Where-Object { $_.ProcessName -like "*java*" } | Where-Object { $_.MainWindowTitle -like "*Minecraft*" -or $_.ProcessName -eq "javaw" }
                if ($minecraft) {
                    Write-Host "Minecraft process detected - launch appears successful!" -ForegroundColor Green
                } else {
                    Write-Host "No Minecraft process detected - you may need to manually click Play" -ForegroundColor Yellow
                }
                
            } else {
                Write-Host "Could not find CurseForge window after waiting" -ForegroundColor Yellow
            }
        } catch {
            Write-Host "UI automation failed, manual navigation needed" -ForegroundColor Yellow
        }
        
        Write-Host ""
        Write-Host "If automation did not work:" -ForegroundColor Cyan
        Write-Host "   1. Go to My Modpacks tab in CurseForge" -ForegroundColor White
        Write-Host "   2. Find modTest(1.20.1)" -ForegroundColor White
        Write-Host "   3. Click Play" -ForegroundColor White
        
    } else {
        Write-Host "CurseForge not found!" -ForegroundColor Red
    }
} else {
    Write-Host "Usage:" -ForegroundColor Yellow
    Write-Host "  .\launch.ps1 -sci      (launch only)" -ForegroundColor White
    Write-Host "  .\launch.ps1 -sci -a   (build + copy + launch)" -ForegroundColor White
    Write-Host "  .\launch.ps1 -h        (help)" -ForegroundColor White
}