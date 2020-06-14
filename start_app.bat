@echo off
@title Sistemas de Informacao - Aplicacao de acesso a base de dados SQL
@cls

if not exist ".\out" echo "Output directory not found. Creating one..."
if not exist ".\out" mkdir ".\out\production\ONGD_PROJECT"

echo "Compiling java files..."
javac -d ./out/production/ONGD_PROJECT -cp ./lib/mssql-jdbc-7.2.2.jre8.jar ./src/main/*java ./src/model/*.java ./src/view/*.java ./src/jdbc/*.java

@cls

java -cp ./lib/mssql-jdbc-7.2.2.jre8.jar;./out/production/ONGD_PROJECT main.App
