#!/usr/bin/env bash
# Copyright 2023 dinosdev.cn.
# SPDX-License-Identifier: Apache-2.0


# use POSIX interface, symlink is followed automatically
BIN_PATH="${BASH_SOURCE-$0}"
BIN_DIR="$(dirname "${BIN_PATH}")"
APP_DIR="$(cd "${BIN_DIR}/.."; pwd)"

echo "APP_DIR:"$APP_DIR

usage="Usage: app.sh (start|stop) <app-name> <args...>"

# if no args specified, show usage
if [ $# -le 1 ]; then
  echo $usage
  exit 1
fi

START_STOP=$1
APP_NAME=$2
shift 2

MALLOC_ARENA_MAX=1

PID_DIR=${PID_DIR:-"/tmp"}

# Attempt to set JAVA_HOME if it is not set
if [ -z $JAVA_HOME ]; then
  # On OSX use java_home (or /Library for older versions)
  if [ "Darwin" == "$(uname -s)" ]; then
    if [ -x /usr/libexec/java_home ]; then
      JAVA_HOME=$(/usr/libexec/java_home)
    else
      export JAVA_HOME="/Library/Java/Home"
    fi
  fi

  # Fail if we did not detect $JAVA_HOME
  if [[ -z $JAVA_HOME ]]; then
    echo "Error: JAVA_HOME is not set and could not be found." 1>&2
    exit 1
  fi
fi
echo "JAVA_HOME:"$JAVA_HOME
JAVA=$JAVA_HOME/bin/java

IDENT_STRING="${USER:-$(whoami)}"
echo "IDENT_STRING:"${IDENT_STRING}

# get log directory
if [ "$APP_LOG_DIR" = "" ]; then
  APP_LOG_DIR="$APP_DIR/logs"
fi

if [ ! -w "$APP_LOG_DIR" ] ; then
  mkdir -p "$APP_LOG_DIR"
  chown $IDENT_STRING $APP_LOG_DIR
fi

LOG_FILE_PRE="$APP_LOG_DIR/$IDENT_STRING-$APP_NAME"
PID=$PID_DIR/$IDENT_STRING-$APP_NAME.pid

ROTATE_LOG ()
{
    log=$1;
    num=5;
    if [ -n "$2" ]; then
	    num=$2
    fi
    if [ -f "$log" ]; then # rotate logs
	    while [ $num -gt 1 ]; do
        prev=`expr $num - 1`
        [ -f "$log.$prev" ] && mv "$log.$prev" "$log.$num"
        num=$prev
	    done
	    mv "$log" "$log.$num";
    fi
}

RUN_JAVA ()
{
  OUT_FILE=$1
  shift
  APP_JAVA_OPTS="$APP_JAVA_OPTS -server -Xmx${MEMORY_SIZE} -XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=32m  -XX:+HeapDumpOnOutOfMemoryError -XX:ErrorFile=$APP_LOG_DIR/jvm-err-%p.log -XX:HeapDumpPath=$APP_LOG_DIR/jvm-dump-heap-%p"
  APP_JAVA_OPTS="$APP_JAVA_OPTS -Djava.net.preferIPv4Stack=true"
  APP_JAVA_OPTS="$APP_JAVA_OPTS -Dlogging.dir=$APP_LOG_DIR -Dlogging.file=$LOG_FILE_PRE"
  APP_JAVA_OPTS="$APP_JAVA_OPTS -Xlog:gc*:file=${LOG_FILE_PRE}.gc.%t.log:tags,time,uptime,level:filecount=30,filesize=5M"

  CLASSPATH=${CLASSPATH}:$APP_DIR'/lib/*'
  CLASSPATH=${CLASSPATH}:$APP_DIR'/conf/*'
  CLASSPATH=${CLASSPATH}:$APP_DIR'/conf/'
  CLASSPATH=${CLASSPATH}:$APP_DIR
  CLASS=${MAIN_CLASS:-"cn.dinodev.spring.core.ApplicationMain"}

  export CLASSPATH=$CLASSPATH

  echo "excute ->JAVA:"${JAVA}
  echo "excute ->APP_NAME:"${APP_NAME}
  echo "excute ->APP_JAVA_OPTS:"${APP_JAVA_OPTS}
  echo "excute ->CLASS:"${CLASS}
  echo "excute ->CLASSPATH:"${CLASSPATH}
  echo "excute ->PID:"${PID}
  echo "excute ->LOG_DIR:"${APP_LOG_DIR}

  if [ "$IN_DOCKER" = "true" ]; then
    echo "run in docker"
    "$JAVA" -Dproc_$APP_NAME $APP_JAVA_OPTS $CLASS  > "$OUT_FILE" "$@" 2>&1
    # write the pid to pid_file
    echo $! > $PID_FILE
  else
    nohup "$JAVA" -Dproc_$APP_NAME $APP_JAVA_OPTS $CLASS  > "$OUT_FILE" "$@" 2>&1 < /dev/null &
    # write the pid to pid_file
    echo $! > $PID_FILE
  fi

}

KILL_PROC ()
{
  process_id=$1
  max_attempts=$2

  # Sleep interval (in seconds)
  sleep_interval=10

  # Initialize counter
  attempt=1

  # Loop to check process status
  while [[ $attempt -le $max_attempts ]]; do
    # Check if process exists
    if ! kill -0 "$process_id" >/dev/null 2>&1; then
      echo "$APP_NAME has terminated."
      break
    fi

    echo "Attempt [$attempt]: $APP_NAME is still running, waiting for ${sleep_interval} seconds..."
    sleep "$sleep_interval"

    # Increment the counter
    attempt=$((attempt + 1))
  done

  # Forcefully terminate the process if it exceeds the maximum attempts
  if [[ $attempt -gt $max_attempts ]]; then
    echo "$APP_NAME did not stop gracefully. Forcibly terminating the process."
    kill -9 "$process_id"
  fi
}

PID_FILE=$PID_DIR/$IDENT_STRING-$APP_NAME.pid
APP_STOP_TIMEOUT=${APP_STOP_TIMEOUT:-600}

case $START_STOP in

  (start)

    [ -w "$PID_DIR" ] ||  mkdir -p "$PID_DIR"

    if [ -f $PID_FILE ]; then
      if kill -0 `cat $PID_FILE` > /dev/null 2>&1; then
        echo $APP_NAME running as process `cat $PID_FILE`.  Stop it first.
        exit 1
      fi
    fi

    ROTATE_LOG "$LOG_FILE_PRE.out"
    echo "starting $APP_NAME"
    cd "$APP_DIR"
    RUN_JAVA "${LOG_FILE_PRE}.out" "$@"

    sleep 3
    tail -f "${LOG_FILE_PRE}.out"

    ;;

  (stop)

    if [ -f $PID_FILE ]; then
      TARGET_PID=`cat $PID_FILE`
      if kill -0 $TARGET_PID > /dev/null 2>&1; then
        echo "stopping $APP_NAME"
        LOOPS=$(echo "scale=0; ($APP_STOP_TIMEOUT + 9) / 10" | bc)
        KILL_PROC $TARGET_PID $LOOPS
      else
        echo "no $APP_NAME to stop"
      fi
      rm -f $PID_FILE
    else
      echo "no $APP_NAME to stop"
    fi
    ;;

  (*)
    echo $usage
    exit 1
    ;;
esac
