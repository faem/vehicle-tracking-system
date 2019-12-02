$(document).ready(function(){
    console.log("jquery working !!");
    $('#msg').focus();
    connect();
    
    $('#msg').keydown(function(evt){
        if(evt.which==13){
            sendmsg();
        }
    });
});

window.onbeforeunload = function () {
    logout();
};
console.log(window);

function _(el){
    return document.getElementById(el);
}

function connect(){
    if("WebSocket" in window){
        var a = "ws://"+server+":4141";
        ws = new WebSocket(a);
        var error = null;

        ws.onopen = function(){}

        ws.onerror = function(err){ 
            console.log("error occurred!!"+err.data);
            alert("the server is not running please come again later :) ");
            logout();
        }
        ws.onmessage = function(e){ 
            console.log(e.data);   
            _('msgframe').innerHTML+= "<div class='recievedcont'><div class='recieved'>"+e.data+"</div></div>";
             animatescroll();
        }
        
        ws.onclose = function(){ console.log("conn closed"); }
     }
}

function animatescroll(){
    setTimeout(function(){ _('msgframe').scrollTop = _('msgframe').scrollHeight;  },100);
}

function logout(){ location.href = 'index.php?logout'; }
         
function openmsg(obj){
    currentip = obj.dataset.ip;
    document.getElementById('recname').innerHTML = $('#'+obj.id).children('span')[0].innerHTML; 
}
         
function sendmsg(){
    var msg = document.getElementById('msg').value;
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
        
        ws.send(currentip+','+msg);
        document.getElementById('msgframe').innerHTML+= "<div class='sendcont'><div class='sent'>"+msg+"</div></div>";
        animatescroll();
        document.getElementById('msg').value = '';
    }
}

function replace(msg , key , cl){
    msg = msg.replace(key,"<span class='smiley "+cl+"'></span>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp");
    return msg ;
}