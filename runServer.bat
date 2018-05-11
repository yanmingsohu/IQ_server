@echo off
title iQ 2008 server.
:loop

set path=%path%;lib
set classpath="lib\sqljdbc.jar"

java -cp %classpath%;bin mainc

echo.
echo.

echo 按下任意键，重新启动 ..
@pause > nul

cls
goto loop
