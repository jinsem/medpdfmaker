@echo off
cd /d "%~dp0"
java -Xms256m -Xmx512m -Dapp.properties.ext=file:med-pdf-maker.properties -jar med-pdf-maker.jar %*
pause