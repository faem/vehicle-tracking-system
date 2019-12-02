$(document).ready(function(){
   $('body').fadeIn();    
   $('#msg').keydown(function(evt){
       if(evt.which == 13) sendmsg();
   });    
});

var currentuser = "headquarter";
if(choice!=5)
	$('#recname').html('Vehicle '+choice);
else{
	var x = document.getElementById("hideit");
    x.style.visibility="hidden";	
	$('#recname').html('Broadcast Message');
}
var firebase = new Firebase("https://transport-tracker-affaa.firebaseio.com");
var db = '';
//$('#userlist #loader').show();
$('#msgframe #loader').show();

firebase.child('users').on('value',function(snapshot){
    db = snapshot.val();
    //updateuserlist(db);
});

firebase.on('child_removed',function(s){
    console.log(s.val());
});
            
/*function updateuserlist(db){
    $('#loader').hide();
    var i =0;
    _('userlist').innerHTML ='';
    for(prop in db){
        if(!db.hasOwnProperty(prop)) continue;
                    
        if(db[prop].user==sessionStorage.getItem('user')){
           _('userlist').innerHTML += "<div id='usermain' style='display:none' data-firebase='"+prop+"' class='usertag'><img class='circle'"+            
            "src='images/catpics/"+i+".jpg'><span>"+db[prop].user+"</span></div>";
            continue;
        }
        _('userlist').innerHTML += "<div id='user"+i+"' class='usertag'><img class='circle' src='images/catpics/"+i%7+".jpg'>"+
        "<span>"+db[prop].user+"</span></div>";
        i++;
    }
}*/

function animatescroll(){
    setTimeout(function(){ _('msgframe').scrollTop = _('msgframe').scrollHeight;  },100);
}

var messages = firebase.child('chat/'+choice);

function sendmsg(){
    console.log('user wants to send message');
    var msg = _('msg').value;
    if(msg!=''){
        msg = replace(msg,'(y)','ok');
        
        msg = replace(msg,':D','happiest');
        msg = replace(msg,'=D','happiest');
        msg = replace(msg,':-D','happiest');
        
        msg = replace(msg,':P','tongue');
        
        msg = replace(msg,':(','sad');
        msg = replace(msg,':-(','sad');
        msg = replace(msg,':[','sad');
        msg = replace(msg,'=(','sad');
        
        msg = replace(msg,':/','halfsad');
        
        msg = replace(msg,':)','happy');
        msg = replace(msg,':-)','happy');
        msg = replace(msg,':]','happy');
        msg = replace(msg,'=)','happy');
        
        msg = replace(msg,'8|','cool');
        msg = replace(msg,':v','dassan');
        
        msg = replace(msg,'-_-','facepalm');
        msg = replace(msg,':poop:','poop');
        msg = replace(msg,':shark:','shark');
        msg = replace(msg,'o_0','doubt');
        msg = replace(msg,":'(",'crying');
        
        msg = replace(msg,":-*",'winkkiss');
        msg = replace(msg,":*",'winkkiss');
        
        
        msg = replace(msg,"B-)",'smart');
        msg = replace(msg,"^_^",'pyaara');
        msg = replace(msg,"(devil)",'wickedhappy');
        
		if(choice==5){
			for(var i=1;i<=4;i++){
				var m = firebase.child('chat/'+i);
				m.push({
					user: "headquarter",
					message : msg
				});
			}
		}		
		
		messages.push({
			user: "headquarter",
			message : msg
		});
		
		
        animatescroll();
        _('msg').value = '';
    }
}
var msgrecap = '';
var msgrefresh = 0;
var flipflop   = 0;
messages.on('value',function(s){
    msgrecap = s.val();
    _('msgframe').innerHTML ='';
    for(message in msgrecap){
        if(!msgrecap.hasOwnProperty(message)) continue;
        if(msgrecap[message].user=="headquarter"){
            _('msgframe').innerHTML += "<div class='sendcont'><div class='sent'>"+msgrecap[message].message+"</div></div>";
        }
        else{
            _('msgframe').innerHTML += "<div class='recievedcont'><div class='recieved'>"+msgrecap[message].message+"</div></div>";
            
            if(msgrefresh >0)_('msgincoming').play();
        }
        console.log(msgrecap[message].user);
    }
    animatescroll();
    msgrefresh++;
});

function replace(msg , key , cl){
    msg = msg.replace(key,"<span class='smiley "+cl+"'></span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
    return msg ;
}

function _(el){
    return document.getElementById(el);
}

var fb = firebase.child('users/'+choice+'/uid');

fb.on('value',function(s){
    var uid = s.val();
	var locTime;
	
	var loc = firebase.child('locations/'+uid);
	console.log('locations/'+uid);
	
	loc.on('value',function(s){
		var val = s.val();
		var x = val.speed * 3.6;
		locTime = val.time;
		document.getElementById('speed').innerHTML = x.toFixed(2)+' kph';
	});
	
	var loc2 = firebase.child('distance/'+uid);

	loc2.on('value',function(s){
		var val = s.val();
		var x = val.dist/1000.0;
		var y = val.dist24/1000.0;
		
		document.getElementById('dist').innerHTML = x.toFixed(2)+' k.m.';
		document.getElementById('dist24').innerHTML = y.toFixed(2)+' k.m.';
		
		var time = locTime - val.lastTime;
		console.log("time="+time);
		if(time>86400000){
			loc2.child("dist24").set(0);
			loc2.child("lastTime").set(locTime);	
			var ref = firebase.child('chat/'+choice);
			ref.remove();
		}
			
		
	});
	
	
});


