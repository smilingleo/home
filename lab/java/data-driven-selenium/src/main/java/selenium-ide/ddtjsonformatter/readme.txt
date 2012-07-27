Zuora Data Driven Selenium Test Framework
==========================================

1. Selenium-IDE plugin
	a. zip the ddtjsonformatter in zip format with extension name of 'xpi' (don't include the folder of ddtjsonformatter itself) 
	b. install this selenium addon.
	
2. How to create a new test case
	a. record your test case with selenium-ide, make sure you can replay the test case
	b. 'Export Test Case As' --> 'JSON Formatter of Zuora Selenium DDT', for example, mytestcase.csv
	c. copy and paste the file, mytestcase.csv to /test/selenium/importfile/zuora_ddt_file
	d. run DataDrivenSeleniumSimpleTest