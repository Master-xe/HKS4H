#!/bin/bash

export CLASSPATH=/opt/portalcfdi:/opt/portalcfdi/mysql-connector-8.0.31.jar:/opt/portalcfdi/json-20220924.jar
export JAVA_HOME=/usr/local/java/jdk-17.0.5

$JAVA_HOME/bin/java -cp ".:$CLASSPATH" CancellationRequests

