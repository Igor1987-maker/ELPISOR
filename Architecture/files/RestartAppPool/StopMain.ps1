Invoke-Command -ComputerName "frontqa2016" -ScriptBlock { Stop-WebAppPool -Name "EdUIWebServices" }