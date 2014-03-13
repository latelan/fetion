<?php
// fetion by php

$host_url = "http://f.10086.cn/im5/";
$cookiejar = "cookie.txt";
global $myid;
Get($host_url,$cookiejar);
Login("TEL","PASS", $cookiejar);
SearchId("TO", $cookiejar);

function SearchId($key, $cookiejar)
{
	$timestamp = time()+3600;
	$url="http://f.10086.cn/im5/index/searchFriendsByQueryKey.action";
	$postdata="queryKey=$key";
	$headrefer="Referer: http://f.10086.cn/im5/login/login.action?mnative=0&t=$timestamp";
	#$res = PostwithHeader($url, $postdata, "cookie.txt", $headrefer);
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL,$url);
	curl_setopt($ch, CURLOPT_COOKIEFILE, $cookiejar);
	#curl_setopt($ch, CURLOPT_HEADER, $header);
	
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,0);
	curl_setopt($ch, CURLOPT_POST,1);
	curl_setopt($ch, CURLOPT_POSTFIELDS,$postdata);
	$data = curl_exec($ch);

	#echo $data;
	if(curl_errno($ch)){
		print curl_error($ch);
	}
	$res = preg_match('/idContact":([0-9]+)/', $data, $out);
	$to = "";
	if($res > 0){
		$to = $out[1];
	}
	else
	{
		$to = $myid;
	}
	curl_close($ch);a
	return $to;
}
function SendMsg($to, $msg)
{

}
function Login($userid, $pass, $cookiejar)
{	
	$timestamp = time()+3600;
	$url = "http://f.10086.cn/im5/login/loginHtml5.action?t=$timestamp";
	$postdata = "m=$userid&pass=$pass&captchaCode=&checkCodeKey=null";

	//Post($url,$postdata,"cookie.txt");
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL,$url);
	curl_setopt($ch, CURLOPT_COOKIEFILE, $cookiejar);
	curl_setopt($ch, CURLOPT_COOKIEJAR, $cookiejar);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,true);
	curl_setopt($ch, CURLOPT_POST,1);
	curl_setopt($ch, CURLOPT_POSTFIELDS,$postdata);
	$data = curl_exec($ch);

	if(curl_errno($ch)){
		print curl_error($ch);
	}
	$res = preg_match('/idUser":"([0-9]+)/', $data, $out1);
	if($res > 0)
	{
		$myid = $out1[1];
	}
	curl_close($ch);
}

function Get($url, $cookiejar)
{
	$host_url = "http://f.10086.cn/im5/";
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL,$host_url);
	curl_setopt($ch, CURLOPT_COOKIEJAR, $cookiejar);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,true);
	curl_exec($ch);
	
	if(curl_errno($ch)){
		print curl_error($ch);
	}
	curl_close($ch);
}
?>
