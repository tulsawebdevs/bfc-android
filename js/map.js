var db = openDatabase('myDatabase', '1.0', 'myDatabase', 10000000);
function loadmap()
	{
	navigator.geolocation.getCurrentPosition(function(position) {
		//alert('itworked');
		latlong = position.coords.latitude+','+ position.coords.longitude
		//latlong =google.maps.LatLng(position.coords.latitude,position.coords.longitude);
		map = $("#map").gmap({'center':latlong, 'zoom':15})
	});	
	db.transaction(function (tx) {
		tx.executeSql('SELECT * FROM ZPHOTO', [], function (tx, results) {
	  		var len = results.rows.length, i;
	  		for (i = 0; i < len; i++) {
				//var content = results.rows.item(i).ZDESC + "<img src='images/"+results.rows.item(i).ZCONTENTDMNUMBER+".jpg' style='width:100px;height:100px;'></img>"
	    			$('#map').gmap('addMarker', {'position':results.rows.item(i).ZCOORD_LAT+','+results.rows.item(i).ZCOORD_LONG})//.click(function() {
                		//	$('#map').gmap('openInfoWindow', { 'content': content }, this);
        			//}); 
	  		}
		});
	});
}	
