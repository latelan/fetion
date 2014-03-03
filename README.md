Name: SendMsg
Product Date: 2013-07
Function:
	You can send a message to your mobile with it.
Should need:
	You need to have a fetion account before you using this program.
Steps:
	First,you need to create a config file named _Setting.txt,and write content as follows:
	--------------_Setting.txt-----------
	#1 your moblie number
	#2 your fetion password 
	#3 the unique number of your fetion
 	
	here is a example:
	--------------_Setting.txt-----------
	183xxxx2374
	123456
	982376456
 	
	Attention: the file does not contain the first line,something about last item,usually,you can find it at C:\Documents and Settings\%USER%\Application Data\Fetion under this path,there is a directory which named whith a number,the vary number is the unique number of your fetion,just is the last item you would write in file _Setting.txt.
	
	Second,use the program Encode to encrypt the _Setting.txt,use like this:"java Encode filepath",the filepath in command line is the path of _Setting.txt.Then you will get a file named _Setting.dat in current directory.Put the encrypted file to the directory in which you will run the SendMsg,and delete file _Setting.txt for security.

	Third,now,it is time to appreciate its function,the program SendMsg need one or two arguments at least to run,the first arguments is the short message you would like to send ,and the second is added information,usually you donn't need it.use follow command line pattern,

	"java -Djava.ext.dirs=libpath classpath shortmsg extras"

	Arguments: 
	libpath 	- the path of directory lib,it is necessary;
	classpath 	- the path of the program SendMsg;
	shortmsg	- the content would to be sended;
	extras		- the added information,can be omitted.

