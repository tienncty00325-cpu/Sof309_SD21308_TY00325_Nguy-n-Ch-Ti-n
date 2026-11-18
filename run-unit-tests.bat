@echo off
powershell -ExecutionPolicy Bypass -File "%~dp0scripts\run-unit-tests.ps1"
exit /b %errorlevel%
