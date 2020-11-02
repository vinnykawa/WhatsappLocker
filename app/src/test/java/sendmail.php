<?php 


$to  = $_GET['to'];
$reset_code = $_GET['code'];

$message = "Hello ,\n\n Your Whatsapp Locker reset code is ${reset_code}. \n\nRegards , \nThe Jello Company.";
$headers = "From:thejellocompany@gmail.com, Reply-to:thejellocompany@gmail.com,";

$mail = mail($to,'Whatsapp Locker Reset Code',$message);

if($mail)
	echo "Yippee i can send mail";
else 
	echo "Oh shit! Need a mailer.";





?>