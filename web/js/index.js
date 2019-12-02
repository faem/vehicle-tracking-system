$(document).ready(function(){
     if(sessionStorage.getItem('user')!=null) location.href = 'client.html';
     else $('body').fadeIn();
});
            
            
var firebase = new Firebase("https://transport-tracker-affaa.firebaseio.com");
var db = '';
            
firebase.child('users').on('value',function(s){ db = s.val()});
                        
function val(el){
    return document.getElementById(el).value;
}
            
function _(el){
    return document.getElementById(el);
}
            
function log(a){
    console.log(a);
}
var registeruser = '';
            
function submit(){
     registeruser = setInterval(function(){
     if(db!=''){                      
          for(prop in db){
             if(!db.hasOwnProperty(prop)) continue;
                         
             if(db[prop].user==val('uname')) namentavail(val('uname'));
             else if(val('uname').length>4&&val('uname').match(/[a-z]/i)){  
                    firebase.child('users').push({user:val('uname')});
                    sessionStorage.setItem('user',val('uname'));
                    _('uname').value = '';
                    location.href = 'index.html';
            }   
            clearInterval(registeruser);  
          }
        }
     },10);    
}
            
function namentavail(nm){
     console.log('name not availables');
     $('#uname').css({width:'45%'});
     setTimeout(function(){ $('#arrow').fadeIn(); },800);
}
            
$('#uname').keydown(function(evt){
     if(evt.which==13){ if(val('uname').length>3) submit();}
     $('#arrow').hide();
     $('#uname').css({width:'100%'});
});