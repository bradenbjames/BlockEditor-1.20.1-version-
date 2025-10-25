# Create tinted glass user block assets
for ($i = 1; $i -le 20; $i++) {
    # Create blockstate file
    $blockstateContent = @"
{
  "variants": {
    "": {
      "model": "blockeditor:block/u_tinted_glass$i"
    }
  }
}
"@
    $blockstateContent | Out-File -FilePath ".\src\main\resources\assets\blockeditor\blockstates\u_tinted_glass$i.json" -Encoding UTF8

    # Create block model file
    $blockModelContent = @"
{
  "parent": "blockeditor:block/dynamic_block_tinted_glass"
}
"@
    $blockModelContent | Out-File -FilePath ".\src\main\resources\assets\blockeditor\models\block\u_tinted_glass$i.json" -Encoding UTF8

    # Create item model file
    $itemModelContent = @"
{
  "parent": "blockeditor:block/u_tinted_glass$i"
}
"@
    $itemModelContent | Out-File -FilePath ".\src\main\resources\assets\blockeditor\models\item\u_tinted_glass$i.json" -Encoding UTF8
}

Write-Host "Created 60 tinted glass asset files (3 files x 20 user blocks)"