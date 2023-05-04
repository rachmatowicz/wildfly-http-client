#! /bin/bash
PID_FILE=pid_file
TEST_SERVER_NAME=LegacyEEInteropEJBTestServer
FQ_TEST_SERVER_NAME=org.wildfly.httpclient.interop.test.LegacyEEInteropEJBTestServer

kill_existing_process () {
  echo "Checking for existing process with name $1"
  existing_server_pid=$(jps | grep $1 | awk '{print $1}')
  if [[ -n $existing_server_pid ]]; then
    echo "Killing stale process $existing_server_pid"
    kill $existing_server_pid
  fi
}

# clear any previous starts
kill_existing_process $TEST_SERVER_NAME
rm $PID_FILE

# check num arguments
if [ "$#" -ne 1 ]; then
  echo "Expecting only one argument, the test classpath, from exec:exec";
  exit 1
fi
# pickup the classpath argument (supplied by <classpath/>)
TEST_CLASSPATH=$1

# pipe all output to null to avoid the background process hanging intermittently
java -classpath $TEST_CLASSPATH $FQ_TEST_SERVER_NAME > legacy.server.log 2>&1 &
server_pid=$!

echo "Started Undertow instance with pid $server_pid"
echo $server_pid > $PID_FILE
