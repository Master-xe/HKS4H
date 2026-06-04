#!/bin/bash

export CLASSPATH=/opt/portalcfdi:/opt/portalcfdi/mysql-connector-8.0.31.jar:/opt/portalcfdi/json-20220924.jar
export JAVA_HOME=/usr/local/java/jdk-17.0.5

$JAVA_HOME/bin/javac UnitTest.java
$JAVA_HOME/bin/javac Cancellations.java
$JAVA_HOME/bin/javac UnzipDownloads.java
$JAVA_HOME/bin/javac CancellationRequests.java
$JAVA_HOME/bin/javac CancellationResponses.java

$JAVA_HOME/bin/javac -cp ".:$CLASSPATH" AppDM.java
#$JAVA_HOME/bin/java -cp ".:$CLASSPATH" AppDM > AppDM.log &

#50 * * * 1-5 sh /opt/portalcfdi/Cancellations.sh
#20 * * * 1-5 sh /opt/portalcfdi/UnzipDownloads.sh
#0 * * * 1-5 sh /opt/portalcfdi/CancellationRequests.sh
#0 12 * * 6,0 sh /opt/portalcfdi/CancellationRequests.sh
#10 * * * 1-5 sh /opt/portalcfdi/CancellationResponses.sh
