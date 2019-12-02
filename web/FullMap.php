<div id="map"></div>

<script src="https://www.gstatic.com/firebasejs/4.12.1/firebase.js"></script>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&callback=initMap">
</script>
<script>
    var map;
    var zoom = 15;
    var marker1, marker2, marker3, marker4;
    var lat_max = -90,
        lat_min = 90,
        lang_max = -180,
        lang_min = 180;
    var choice = 0;

    function initMap() {
        var bounds = new google.maps.LatLngBounds();
        map = new google.maps.Map(document.getElementById('map'), {
            zoom: 14,
            center: {
                lat: 0,
                lng: 0
            }
        });


        marker1 = new google.maps.Marker({
            position: {
                lat: 0,
                lng: 0
            },
            map: map,
            icon: 'images/truck1.png'
        });
        marker2 = new google.maps.Marker({
            position: {
                lat: 0,
                lng: 0
            },
            map: map,
            icon: 'images/truck2.png'
        });
        marker3 = new google.maps.Marker({
            position: {
                lat: 0,
                lng: 0
            },
            map: map,
            icon: 'images/truck3.png'
        });
        marker4 = new google.maps.Marker({
            position: {
                lat: 0,
                lng: 0
            },
            map: map,
            icon: 'images/truck4.png'
        });

        var uid1;
        var user_ref1 = firebase.database().ref("users/1");
        user_ref1.on("value", function(snapshot) {
            //document.getElementById("none").innerHTML = JSON.stringify(snapshot.val(), null, 2);
            var obj = JSON.parse(JSON.stringify(snapshot.val(), null, 2));
            uid1 = obj.uid;
            var ref = firebase.database().ref("locations/" + uid1);
            ref.on("value", function(snapshot) {
                //document.getElementById("none").innerHTML = JSON.stringify(snapshot.val(), null, 2);
                console.log(JSON.stringify(snapshot.val(), null, 2));
                var obj = JSON.parse(JSON.stringify(snapshot.val(), null, 2));
                var latlng = new google.maps.LatLng(obj.latitude, obj.longitude);
                if (choice == 0) {
                    marker1.setPosition(latlng);
                    bounds.extend(latlng);
                    map.fitBounds(bounds);
                } else if (choice == 1) {
                    marker1.setPosition(latlng);
                    map.setZoom(zoom);
                    map.setCenter(latlng);
                }

                /*if(obj.latitude>lat_max)
                	lat_max = obj.latitude;
                else if(obj.latitude<lat_min)
                	lat_min = obj.latitude;
                if(obj.longitude>lang_max)
                	lang_max = obj.latitude;
                else if(obj.longitude<lang_min)
                	lang_min = obj.latitude;
                */
                //map.setCenter(latlng);
            });
        });

        var uid2;
        var user_ref2 = firebase.database().ref("users/2");
        user_ref2.on("value", function(snapshot) {
            //document.getElementById("none").innerHTML = JSON.stringify(snapshot.val(), null, 2);
            var obj = JSON.parse(JSON.stringify(snapshot.val(), null, 2));
            uid2 = obj.uid;
            var ref = firebase.database().ref("locations/" + uid2);
            ref.on("value", function(snapshot) {
                //document.getElementById("none").innerHTML = JSON.stringify(snapshot.val(), null, 2);
                console.log(JSON.stringify(snapshot.val(), null, 2));
                var obj = JSON.parse(JSON.stringify(snapshot.val(), null, 2));
                var latlng = new google.maps.LatLng(obj.latitude, obj.longitude);
                if (choice == 0) {
                    marker2.setPosition(latlng);
                    bounds.extend(latlng);
                    map.fitBounds(bounds);
                } else if (choice == 2) {
                    marker2.setPosition(latlng);
                    map.setZoom(zoom);
                    map.setCenter(latlng);
                }
                //map.setCenter(latlng);
            });
        });

        var uid3;
        var user_ref3 = firebase.database().ref("users/3");
        user_ref3.on("value", function(snapshot) {
            //document.getElementById("none").innerHTML = JSON.stringify(snapshot.val(), null, 2);
            var obj = JSON.parse(JSON.stringify(snapshot.val(), null, 2));
            uid3 = obj.uid;
            var ref = firebase.database().ref("locations/" + uid3);
            ref.on("value", function(snapshot) {
                //document.getElementById("none").innerHTML = JSON.stringify(snapshot.val(), null, 2);
                console.log(JSON.stringify(snapshot.val(), null, 2));
                var obj = JSON.parse(JSON.stringify(snapshot.val(), null, 2));
                var latlng = new google.maps.LatLng(obj.latitude, obj.longitude);
                if (choice == 0) {
                    marker3.setPosition(latlng);
                    bounds.extend(latlng);
                    map.fitBounds(bounds);
                } else if (choice == 3) {
                    marker3.setPosition(latlng);
                    map.setZoom(zoom);
                    map.setCenter(latlng);
                }
                //map.setCenter(latlng);
            });
        });

        var uid4;
        var user_ref4 = firebase.database().ref("users/4");
        user_ref4.on("value", function(snapshot) {
            //document.getElementById("none").innerHTML = JSON.stringify(snapshot.val(), null, 2);
            var obj = JSON.parse(JSON.stringify(snapshot.val(), null, 2));
            uid4 = obj.uid;
            var ref = firebase.database().ref("locations/" + uid4);
            ref.on("value", function(snapshot) {
                //document.getElementById("none").innerHTML = JSON.stringify(snapshot.val(), null, 2);
                console.log(JSON.stringify(snapshot.val(), null, 2));
                var obj = JSON.parse(JSON.stringify(snapshot.val(), null, 2));
                var latlng = new google.maps.LatLng(obj.latitude, obj.longitude);
                if (choice == 0) {
                    marker4.setPosition(latlng);
                    bounds.extend(latlng);
                    map.fitBounds(bounds);
                } else if (choice == 4) {
                    marker4.setPosition(latlng);
                    map.setZoom(zoom);
                    map.setCenter(latlng);
                }
                //map.setCenter(latlng);
            });
        });

    }
</script>
<script>

function button1Clicked() {
    choice = 1;
	initMap();
	marker2.setPosition(null);
	marker3.setPosition(null);
	marker4.setPosition(null);
	map.setCenter(marker1.getPosition());
	map.setZoom(zoom);
}

function button2Clicked() {
    choice = 2;
	initMap();
	marker1.setPosition(null);
	marker3.setPosition(null);
	marker4.setPosition(null);
	map.setCenter(marker2.getPosition());
	map.setZoom(zoom);
}

function button3Clicked() {
    choice = 3;
	initMap();
	marker2.setPosition(null);
	marker1.setPosition(null);
	marker4.setPosition(null);
	map.setCenter(marker3.getPosition());
	map.setZoom(zoom);
}

function button4Clicked() {
    choice = 4;
	initMap();
	marker2.setPosition(null);
	marker3.setPosition(null);
	marker1.setPosition(null);
	map.setCenter(marker4.getPosition());
	map.setZoom(zoom);
}

function buttonAllClicked() {
	choice = 0;
    initMap();
}

</script>

<script>
    var config = {
        apiKey: "AIzaSyBJvwT7PZ6Pz49g_Za1RL3KH0kdXmpcTUU",
        authDomain: "transport-tracker-affaa.firebaseapp.com",
        databaseURL: "https://transport-tracker-affaa.firebaseio.com",
        projectId: "transport-tracker-affaa",
        storageBucket: "transport-tracker-affaa.appspot.com",
        messagingSenderId: "490432028798"
    };
    firebase.initializeApp(config);
	
	var choice = "<?php echo $_GET['choice']?>";
	if(choice == 0)
		buttonAllClicked();
	else if(choice == 1)
		button1Clicked();
	else if(choice == 2)
		button2Clicked();
	else if(choice == 3)
		button3Clicked();
	else if(choice == 4)
		button4Clicked();
</script>




<style>
    /* Always set the map height explicitly to define the size of the div
     * element that contains the map. */

    #map {
        height: 100%;
    }

    /* Optional: Makes the sample page fill the window. */

    html,
    body {
        height: 100%;
        margin: 0;
        padding: 0;
    }
</style>