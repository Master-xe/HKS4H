#!/bin/bash
export CLASSPATH=/opt/portalcfdi/json-20220924.jar
export JAVA_HOME=/usr/local/java/jdk-25.0.5.0.3
$JAVA_HOME/bin/javac -cp ".:$CLASSPATH" *.java
