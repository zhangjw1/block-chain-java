@echo off
echo Compiling Java Blockchain Learning Project...
echo.

REM 检查Java环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 8 or higher
    pause
    exit /b 1
)

REM 创建输出目录
if not exist "target\classes" mkdir "target\classes"

REM 编译Java文件
echo Compiling Java source files...
javac -cp "src\main\java" -d "target\classes" src\main\java\com\blockchain\learning\*.java src\main\java\com\blockchain\learning\config\*.java src\main\java\com\blockchain\learning\model\*.java src\main\java\com\blockchain\learning\service\*.java

if %errorlevel% neq 0 (
    echo Error: Compilation failed
    echo Please install Maven to properly manage dependencies
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo Note: To run the application, you need Maven to manage dependencies.
echo Please install Maven and run: mvn spring-boot:run
pause