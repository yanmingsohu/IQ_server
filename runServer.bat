@echo off
title iQ 2008 server.
:loop

set path=%path%;lib
set classpath="lib\sqljdbc.jar"

java -cp %classpath%;bin mainc

echo.
echo.

echo ������������������� ..
@pause > nul

cls
goto loop
