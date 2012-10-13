var db = openDatabase('myDatabase', '1.0', 'myDatabase', 10000000);
function loadmap()
	{
	navigator.geolocation.getCurrentPosition(function(position) {
		//alert('itworked');
		latlong = position.coords.latitude+','+ position.coords.longitude
		//latlong =google.maps.LatLng(position.coords.latitude,position.coords.longitude);
		map = $("#map").gmap({'center':latlong, 'zoom':10})
	});	
	db.transaction(function (tx) {
		tx.executeSql('SELECT * FROM ZPHOTO', [], function (tx, results) {
	  		var len = results.rows.length, i;
	  		for (i = 0; i < len; i++) {
	    			$('#map').gmap('addMarker', {'position':results.rows.item(i).ZCOORD_LAT+','+results.rows.item(i).ZCOORD_LONG});
	  		}
		});
	});
}	
