# ElectrumTaxCalculator

**I no longer recommend Electrum as a cryptocurrency wallet, [see why](https://github.com/spesmilo/electrum/issues/5183).**

**If you choose to use it anyway, be careful to use version 3.3.3 or higher (beware the version 4.0.0 scam!) and do not follow any auto-upgrade links.**

As a cryptocurrency miner, you must pay taxes on cost-basis sales for capital gains AND for mining revenue.

Generates two reportable tax figures from your Electrum-exported transaction history by pinging API's for historical price quotes.
Other figures you may want to calculate seperately are the capital investment in your GPUs/ASICs and the electricity cost you can deduct.

THIS PROGRAM TAKES 2 COMMAND-LINE ARGUMENTS:

java -jar ElectrumTaxCalculator.jar [csv filename with extension] [year to calculate filing]

Ensure a Java JRE is installed on your machine and is in the PATH: https://www.java.com/en/download/help/path.xml

First, export your entire Electrum transaction history. If you use another wallet, you can import your priv-key into Electrum first to perform this reporting.

Add a column for indicating whether a +transaction was the result of your own mining and fill it out. Cells default to "no" and are disregarded for other years.

Verify that none of your transaction labels contain commas and that the file is in the format shown in the PNG.

Run the script as shown above.
