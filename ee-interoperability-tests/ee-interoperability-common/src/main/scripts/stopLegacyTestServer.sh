#! /bin/bash
PID_FILE=pid_file

echo "Stopping Undertow instance with process id `cat $PID_FILE`"
kill -9 `cat $PID_FILE`
