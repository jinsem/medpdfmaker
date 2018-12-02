@echo off
cd /d "%~dp0"
java -Dapp.properties.ext=file:med-pdf-maker.properties -jar med-pdf-maker.jar %*
pause