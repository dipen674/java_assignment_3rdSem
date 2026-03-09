@echo off
echo --- Initializing Hall Booking Management System ---

if not exist bin mkdir bin

echo Compiling source files...
dir /s /b src\*.java > sources.txt
javac -d bin @sources.txt

if %ERRORLEVEL% NEQ 0 (
    echo Error: Compilation failed. Please check your Java environment.
    pause
    exit /b 1
)

echo Compilation successful!
echo Launching application...
echo --------------------------------------------------
java -cp bin com.hallsymphony.ui.MainFrame
pause
