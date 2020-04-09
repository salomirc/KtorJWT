#!/bin/bash
until ./gradlew run; do
    echo "'gradlew run' crashed with exit code $?. Restarting..." >&2
    sleep 1
done
