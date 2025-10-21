param(
    [switch]$sci,
    [switch]$a,
    [switch]$h
)

if ($h) {
    Write-Host "Simple Minecraft Instance Launcher" -ForegroundColor Green
    Write-Host "Usage:" -ForegroundColor Yellow
    Write-Host "  .\launch-simple.ps1 -sci      (build + copy + launch instance)" -ForegroundColor White
    Write-Host "  .\launch-simple.ps1           (just launch instance)" -ForegroundColor White
    exit
}

$instancePath = "C:\Users\brade\curseforge\minecraft\Instances\modTest(1.20.1)"
$cf = "C:\Users\brade\AppData\Local\Programs\CurseForge Windows\CurseForge.exe"

# Build and copy if requested
if ($sci -or $a) {
    Write-Host "Building mod..." -ForegroundColor Yellow
    $buildResult = & .\gradlew.bat build
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Build complete!" -ForegroundColor Green
        
        Write-Host "Copying mod..." -ForegroundColor Yellow
        $source = "build\libs\be-1.0.2.jar"
        $modsFolder = "$instancePath\mods"
        $destination = "$modsFolder\be-1.0.2.jar"
        
        # Remove any existing mod files (be-*.jar or blockeditor-*.jar)
        if (Test-Path $modsFolder) {
            $existingMods = @()
            $existingMods += Get-ChildItem -Path $modsFolder -Filter "be-*.jar" -ErrorAction SilentlyContinue
            $existingMods += Get-ChildItem -Path $modsFolder -Filter "blockeditor-*.jar" -ErrorAction SilentlyContinue
            
            foreach ($mod in $existingMods) {
                Write-Host "Removing existing mod: $($mod.Name)" -ForegroundColor Yellow
                Remove-Item $mod.FullName -Force
            }
        }
        
        if (Test-Path $source) {
            Copy-Item $source $destination -Force
            Write-Host "Copy complete!" -ForegroundColor Green
        } else {
            Write-Host "Mod file not found!" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "Build failed!" -ForegroundColor Red
        exit 1
    }
}

# Launch the specific instance
Write-Host "Launching modTest(1.20.1) instance..." -ForegroundColor Cyan

if (Test-Path $instancePath) {
    if (Test-Path $cf) {
        # Try multiple launch methods
        
        # Method 1: Direct instance launch with instance ID
        Write-Host "Attempting direct instance launch..." -ForegroundColor Green
        try {
            # Use the instance ID instead of path for CurseForge --launch-instance
            $instanceId = "e00bc721-29fa-4862-b80e-15209a1f6883"  # modTest(1.20.1) instance ID
            $cfArgs = @("--launch-instance", $instanceId)
            Start-Process $cf -ArgumentList $cfArgs
            Write-Host "Launch command sent!" -ForegroundColor Green
            
            # Wait for CurseForge to load
            Start-Sleep -Seconds 2
            Write-Host "Waiting for CurseForge to load..." -ForegroundColor Yellow
            
            # Look for CurseForge window and try to click play button
            Add-Type -AssemblyName System.Windows.Forms
            Add-Type -AssemblyName System.Drawing
            
            # Wait a bit more for the interface to load
            Start-Sleep -Seconds 1
            
            # Try to find and click the play button using UI automation
            Write-Host "Looking for CurseForge window..." -ForegroundColor Yellow
            $curseWindow = Get-Process | Where-Object { $_.ProcessName -eq "CurseForge" -and $_.MainWindowTitle -ne "" }
            
            if ($curseWindow) {
                Write-Host "Found CurseForge window, attempting to activate..." -ForegroundColor Green
                # Bring CurseForge to front
                Add-Type -TypeDefinition @"
                    using System;
                    using System.Runtime.InteropServices;
                    public class Win32 {
                        [DllImport("user32.dll")]
                        public static extern bool SetForegroundWindow(IntPtr hWnd);
                        [DllImport("user32.dll")]
                        public static extern bool ShowWindow(IntPtr hWnd, int nCmdShow);
                        [DllImport("user32.dll")]
                        public static extern bool GetWindowRect(IntPtr hWnd, out RECT lpRect);
                        [DllImport("user32.dll")]
                        public static extern bool SetCursorPos(int x, int y);
                        [DllImport("user32.dll")]
                        public static extern void mouse_event(uint dwFlags, uint dx, uint dy, uint dwData, int dwExtraInfo);
                        
                        [StructLayout(LayoutKind.Sequential)]
                        public struct RECT {
                            public int Left;
                            public int Top;
                            public int Right;
                            public int Bottom;
                        }
                    }
"@
                # First maximize the window to ensure consistent layout
                Write-Host "Maximizing CurseForge window..." -ForegroundColor Cyan
                [Win32]::ShowWindow($curseWindow.MainWindowHandle, 3) # SW_MAXIMIZE
                Start-Sleep -Seconds 2
                
                # Then bring it to foreground
                [Win32]::SetForegroundWindow($curseWindow.MainWindowHandle)
                
                Start-Sleep -Seconds 3
                
                # Try to click on the modTest(1.20.1) instance first
                Write-Host "Attempting to start instance..." -ForegroundColor Yellow
                
                # Get window rectangle for click coordinates
                $rect = New-Object Win32+RECT
                [Win32]::GetWindowRect($curseWindow.MainWindowHandle, [ref]$rect)
                
                # Calculate position for the modTest(1.20.1) instance (center area with BE logo)
                $windowWidth = $rect.Right - $rect.Left
                $windowHeight = $rect.Bottom - $rect.Top
                
                # Based on the image, modTest(1.20.1) is in the top row, center-right position
                $instanceX = $rect.Left + ($windowWidth * 0.48)  # Center area where modTest(1.20.1) is
                $instanceY = $rect.Top + ($windowHeight * 0.30)  # Top row of instances
                
                Write-Host "Looking for modTest(1.20.1) with BE logo at: $instanceX, $instanceY" -ForegroundColor Cyan
                
                # First click to select the instance
                [Win32]::SetCursorPos([int]$instanceX, [int]$instanceY)
                Start-Sleep -Milliseconds 50
                [Win32]::mouse_event(0x02, 0, 0, 0, 0) # Left button down
                [Win32]::mouse_event(0x04, 0, 0, 0, 0) # Left button up
                
                Write-Host "Instance clicked, moving to play button..." -ForegroundColor Yellow
                Start-Sleep -Milliseconds 200
                
                # Hover over the instance to reveal the play button
                Write-Host "Hovering to reveal play button..." -ForegroundColor Cyan
                [Win32]::SetCursorPos([int]$instanceX, [int]$instanceY)
                Start-Sleep -Milliseconds 300
                
                # Now click the orange Play button in the top-right area
                Write-Host "Clicking Play button..." -ForegroundColor Green
                # The Play button appears in the top-right, more precisely positioned
                $playButtonX = $rect.Left + ($windowWidth * 0.72)  # Adjusted to be more left of the far edge
                $playButtonY = $rect.Top + ($windowHeight * 0.17)  # Slightly lower in the header area
                
                [Win32]::SetCursorPos([int]$playButtonX, [int]$playButtonY)
                Start-Sleep -Milliseconds 50
                
                # Single click the Play button
                [Win32]::mouse_event(0x02, 0, 0, 0, 0) # Left button down
                [Win32]::mouse_event(0x04, 0, 0, 0, 0) # Left button up
                
                Write-Host "Waiting for instance to launch..." -ForegroundColor Yellow
                Start-Sleep -Seconds 5
                
                # Check if Minecraft started
                Start-Sleep -Seconds 8
                $minecraft = Get-Process | Where-Object { $_.ProcessName -like "*java*" -or $_.ProcessName -eq "javaw" }
                if ($minecraft) {
                    Write-Host "Minecraft is starting!" -ForegroundColor Green
                } else {
                    Write-Host "Launch attempted - check CurseForge if Minecraft didn't start" -ForegroundColor Yellow
                }
            } else {
                Write-Host "CurseForge window not found, check manually" -ForegroundColor Yellow
            }
            
        } catch {
            Write-Host "Direct launch failed: $($_.Exception.Message)" -ForegroundColor Red
            Write-Host "Trying fallback method..." -ForegroundColor Yellow
            
            # Method 2: Launch CurseForge and let user click
            Start-Process $cf
            Write-Host "CurseForge opened - your instance should be ready to launch" -ForegroundColor Green
        }
    } else {
        Write-Host "CurseForge not found at: $cf" -ForegroundColor Red
    }
} else {
    Write-Host "Instance path not found: $instancePath" -ForegroundColor Red
}

Write-Host "Done!" -ForegroundColor Green