#!/bin/bash

export JAVA_HOME=/usr/local/java/jdk-17.0.5
export CLASSPATH=/opt/portalcfdi:/opt/portalcfdi/mysql-connector-8.0.31.jar

$JAVA_HOME/bin/java -cp ".:$CLASSPATH" KoneshIntegratorService >> KoneshIntegratorService.log

