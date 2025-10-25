# Comprehensive Block Reorganization Script
# This script organizes all block types into folders and fixes all references

Write-Host "Starting comprehensive block reorganization..." -ForegroundColor Green

# Define all block types based on existing files
$blockTypes = @(
    "glass", "tinted_glass", "stained_glass", "stone", "dirt", "sand", "wood", "wool", 
    "concrete", "deepslate", "cobblestone", "smooth_stone", "calcite", "diorite", 
    "terracotta", "concrete_powder", "dead_tube_coral", "mushroom_stem", "pearlescent_froglight"
)

$basePath = ".\src\main\resources\assets\be"
$modelsPath = "$basePath\models\block"
$blockstatesPath = "$basePath\blockstates"

foreach ($blockType in $blockTypes) {
    Write-Host "Processing block type: $blockType" -ForegroundColor Yellow
    
    # Create folder for this block type
    $blockTypeFolder = "$modelsPath\$blockType"
    if (!(Test-Path $blockTypeFolder)) {
        New-Item -ItemType Directory -Path $blockTypeFolder -Force | Out-Null
        Write-Host "  Created folder: $blockTypeFolder"
    }
    
    # Move main dynamic block model
    $mainModel = "$modelsPath\dynamic_block_$blockType.json"
    if (Test-Path $mainModel) {
        Move-Item $mainModel "$blockTypeFolder\" -Force
        Write-Host "  Moved main model: dynamic_block_$blockType.json"
    }
    
    # Move user variant models
    $userModels = Get-ChildItem "$modelsPath\u_$blockType*.json" -ErrorAction SilentlyContinue
    foreach ($userModel in $userModels) {
        Move-Item $userModel.FullName "$blockTypeFolder\" -Force
        Write-Host "  Moved user model: $($userModel.Name)"
    }
    
    # Update main blockstate file
    $mainBlockstate = "$blockstatesPath\dynamic_block_$blockType.json"
    if (Test-Path $mainBlockstate) {
        $content = Get-Content $mainBlockstate -Raw
        $newContent = $content -replace "be:block/dynamic_block_$blockType", "be:block/$blockType/dynamic_block_$blockType"
        Set-Content $mainBlockstate $newContent
        Write-Host "  Updated main blockstate: dynamic_block_$blockType.json"
    }
    
    # Update user variant blockstates
    $userBlockstates = Get-ChildItem "$blockstatesPath\u_$blockType*.json" -ErrorAction SilentlyContinue
    foreach ($userBlockstate in $userBlockstates) {
        $content = Get-Content $userBlockstate.FullName -Raw
        $fileName = [System.IO.Path]::GetFileNameWithoutExtension($userBlockstate.Name)
        $newContent = $content -replace "be:block/$fileName", "be:block/$blockType/$fileName"
        Set-Content $userBlockstate.FullName $newContent
        Write-Host "  Updated user blockstate: $($userBlockstate.Name)"
    }
    
    # Update user variant models to reference correct parent
    $userModelsInFolder = Get-ChildItem "$blockTypeFolder\u_$blockType*.json" -ErrorAction SilentlyContinue
    foreach ($userModel in $userModelsInFolder) {
        $content = Get-Content $userModel.FullName -Raw
        $newContent = $content -replace "be:block/dynamic_block_$blockType", "be:block/$blockType/dynamic_block_$blockType"
        Set-Content $userModel.FullName $newContent
        Write-Host "  Updated user model parent reference: $($userModel.Name)"
    }
    
    # Update item model
    $itemModel = "$basePath\models\item\dynamic_block_$blockType.json"
    if (Test-Path $itemModel) {
        $content = Get-Content $itemModel -Raw
        $newContent = $content -replace "be:block/dynamic_block_$blockType", "be:block/$blockType/dynamic_block_$blockType"
        Set-Content $itemModel $newContent
        Write-Host "  Updated item model: dynamic_block_$blockType.json"
    }
}

Write-Host "Block reorganization complete!" -ForegroundColor Green
Write-Host "All block types now have organized folder structure with correct references." -ForegroundColor Green