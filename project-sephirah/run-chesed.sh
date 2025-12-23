#!/bin/bash
# Run Project Sephirah and open Chesed module

cd "$(dirname "$0")"

echo "🚀 Starting Project Sephirah..."
echo "📌 Sending '1' to open Chesed module"
echo ""

# Compile first
mvn clean compile -q

# Run with input: "1" to select Chesed, then "0" to exit
echo -e "1\n0" | mvn exec:java -Dexec.mainClass="com.atziluth.ProjectSephirah" -q
