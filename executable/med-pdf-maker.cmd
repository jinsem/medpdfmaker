@echo off
cd /d "%~dp0"
java -jar med-pdf-maker.jar -Dapp.properties.ext=file:med-pdf-maker.properties %*
pause