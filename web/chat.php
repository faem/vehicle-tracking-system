<html> 
    <head>
        <title>Messenger</title>
        <link rel="stylesheet" href="css/client.css">
        <link rel="icon" href="images/favicon.png"/>
        <script src="js/jquery.js"></script>
        <script src="js/firebase.js"></script>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
    </head>
    <body style="display:none">
        <div class="cont">
		<div class="row">
			<div class="col-sm-4" id="hideit" style="padding:50">
			<h1 style="text-align: center;">Details</h1>
			<hr>
			<p>Distance Traveled: <span id="dist"></span></p>
			<p>Distance Traveled (24hr): <span id="dist24"></span></p>
			<p>Speed: <span id="speed"></span></p>
			</div>
           <div class="msgpanel col-sm-7">
                <div class="nameband" id='nameband'>
                    <div class="band">
                        <span class='recname' id='recname' ></span>
                    </div>
                </div>
                <div class="msgframe" id='msgframe'>
                    <div class="loader" id='loader'></div>
                </div>
           </div>
		   <div class="col-sm-1">
           </div>
		   </div>
        
        <div class="row">
		<div class="col-sm-4"></div>
		<div class='msgdiv col-sm-6'><input id='msg' type="text" name='msg' placeholder="Type your message in here" class="msginput"></div>
		
        <div class="col-sm-1"><button class="btn" onClick="sendmsg()"><i class="fa fa-send"></i></button></div>
		<div class="col-sm-1"></div>
		</div>
		</div>
		<audio src="assets/msgincoming.mp3" id='msgincoming'></audio>
		<script type="text/javascript">
			var choice = "<?php echo $_GET['choice']?>"; // That's for a string
			console.log("value="+choice);
		</script>
        <script src="js/client.js"></script>
    </body>
</html>
