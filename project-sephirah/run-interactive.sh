#!/bin/bash
# Interactive Project Sephirah with Chesed Module Demo

cd "$(dirname "$0")"

echo "🚀 Building Project Sephirah..."
mvn clean package -DskipTests -q

echo ""
echo "═══════════════════════════════════════════════════════════"
echo "📚 PROJECT SEPHIRAH - CHESED MODULE CONNECTED"
echo "═══════════════════════════════════════════════════════════"
echo ""
echo "✅ Connection Status:"
echo "   • ProjectSephirah.java → ChesedSephirah.java"
echo "   • Main Menu → Chesed Submenu"
echo "   • Chesed Module Features:"
echo "     1️⃣  OOP Concepts Demo"
echo "     2️⃣  Sorting Algorithms Demo"
echo "     3️⃣  Search Algorithms Demo"
echo "     4️⃣  Comparators & Strategies"
echo "     5️⃣  Full Demonstration"
echo ""
echo "═══════════════════════════════════════════════════════════"
echo ""

java -jar target/project-sephirah-1.0.0-all.jar
