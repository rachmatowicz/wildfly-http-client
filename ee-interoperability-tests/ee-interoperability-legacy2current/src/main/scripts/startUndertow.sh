#! /bin/bash
PID_FILE=pid_file
TEST_SERVER_NAME=EEInteropTestServer
TEST_OUTPUT_DIRECTORY=../ee-interoperability-common/target/test-classes

# set -x

# This script expects to be passed a classpath created from maven dependencies only
# One of the dependencies will contain the legacy tests and server in org.wildfly.wildfly-http-client:ee-interoperability-common
# If nothing else were done, it would start a legacy server
# It therefore needs to adjust the classpath by prefixing the test output directory of org.wildfly.wildfly-http-client:ee-interoperability-common
# to start a current version of the server.

kill_existing_undertow_process () {
  echo "Checking for existing process with name $1"
  existing_server_pid=$(jps | grep $1 | awk '{print $1}')
  if [[ -n $existing_server_pid ]]; then
    echo "Killing stale process $existing_server_pid"
    kill $existing_server_pid
  fi
}

# clear any previous starts
kill_existing_undertow_process $TEST_SERVER_NAME
rm $PID_FILE

# check num arguments
if [ "$#" -ne 1 ]; then
  echo "Expecting only one argument, the test classpath, from exec:exec";
  exit 1
fi
# pickup the classpath argument (supplied by <classpath/>) and prefix with the path to the current test server
TEST_CLASSPATH=$TEST_OUTPUT_DIRECTORY:$1

# pipe all output to null to avoid the background process hanging intermittently
java -classpath $TEST_CLASSPATH org.wildfly.httpclient.interop.test.EEInteropTestServer > /dev/null 2>&1 &
# java -classpath $TEST_CLASSPATH org.wildfly.httpclient.interop.test.EEInteropTestServer &
server_pid=$!

echo "Started Undertow instance with pid $server_pid"
echo $server_pid > $PID_FILE
