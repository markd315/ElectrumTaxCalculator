# ElectrumTaxCalculator
As a cryptocurrency miner, you must pay taxes on cost-basis sales for capital gains AND for mining revenue.
Generates two reportable tax figures from your Electrum-exported transaction history by pinging API's for historical price quotes.

THIS PROGRAM TAKES 2 COMMAND-LINE ARGUMENTS:
java -jar ElectrumTaxCalculator.jar [csv filename with extension] [balance at the start of year]

Ensure a Java JRE is installed on your machine and is in the PATH: https://www.java.com/en/download/help/path.xml
First, export your entire Electrum transaction history. If you use another wallet, you can import your priv-key into Electrum first to perform this reporting.
Calculate your balance at the start of this tax year and remember this value, it is a command-line argument.
Then, delete rows such that the workbook contains ONLY transactions from the filing year.
Add a column for indicating whether a +transaction was the result of your own mining and fill it out. Cells default to "no"
Verify that none of your transaction labels contain commas and that the file is in the format shown in the PNG.
Run the script as shown above.
