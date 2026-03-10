#!/bin/bash

# Hall Booking Management System Run Script

# 1. Configuration
SRC_DIR="src"
BIN_DIR="bin"
MAIN_CLASS="HallSymphony"

# 2. Setup
echo "--- Initializing Hall Booking Management System ---"
mkdir -p "$BIN_DIR"

# 3. Compilation
echo "Compiling source files..."
find "$SRC_DIR" -name "*.java" > sources.txt
echo "HallSymphony.java" >> sources.txt
javac -d "$BIN_DIR" @sources.txt

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Launching application..."
    echo "--------------------------------------------------"
    # 4. Execution
    java -cp "$BIN_DIR" "$MAIN_CLASS"
else
    echo "Error: Compilation failed. Please check your Java environment."
    exit 1
fi
