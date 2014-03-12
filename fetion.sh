#!/bin/bash

if [ $# -eq 0 ]; then
	echo "Usage: fetion [-u name|tel] -m Message"
	exit 1
fi

while [ $# -gt 0 ]
do
	case $1 in
	-u)	destination=$2
	shift
		;;
	-m) message=$2
	shift
		;;
	-*) echo "Usage: fetion [-u name|tel] -m Message"
		exit 1
		;;
	*)	break
		;;
	esac
	shift
done

user_id="TEL"
password="PASSWD"

##############################################################
#get cookie
curl -c cookie.txt http://f.10086.cn/im5/ &> /dev/null

#login
post_data="m=$user_id&pass=$password&captchaCode=&checkCodeKey=null"
timestamp=$((`date +%s`+3600))
url="http://f.10086.cn/im5/login/loginHtml5.action?t=$timestamp"
curl -b cookie.txt -c cookie.txt -d $post_data $url &> /dev/null

###################################################################
#name->userid tel->userid

url="http://f.10086.cn/im5/index/searchFriendsByQueryKey.action"
post_data="queryKey=$destination"
head_referer="Referer: http://f.10086.cn/im5/login/login.action?mnative=0&t=$timestamp"
result=`curl -s -b cookie.txt -H $head_referer -d $post_data  $url`
echo $result > temp
result=`grep -o -E 'idContact":[0-9]+' | sed 's/.*://g' < $result`
if [ -n "$result" ]; then
	userid=$result
	echo "Id: $userid"
else
	userid="ID" #自己的ID,可通过添加上面注释行在temp中找到
	echo "User ID does not exist!"
	echo "\_[This message will be sent to your moblie phone]"
fi
#######################################################################
#seny message to myself

#message=${message// /%20}   #replace spaces
message=`sed 's// /%20/'`
echo "Message: $message"
msg_post_data="msg=$message&touserid=$userid"
send_url="http://f.10086.cn/im5/chat/sendNewGroupShortMsg.action"
head_referer="Referer: http://f.10086.cn/im5/login/login.action?mnative=0&t=$timestamp"
result=`curl -s -b cookie.txt -c cookie.txt -H $head_referer -d $msg_post_data $send_url`

result=`echo $result | grep -o -E 'sendCode":"[0-9]{1,3}' | sed 's/.*"//g'`

if [ $result -eq 200 ]; then
	echo "send message success![$result]"
else
	echo "send message failure![$result]"
fi
#The End#
