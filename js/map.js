var db = openDatabase('myDatabase', '1.0', 'myDatabase', 10000000);
function loadmap()
	{
	navigator.geolocation.getCurrentPosition(function(position) {
		//alert('itworked');
		latlong = position.coords.latitude+','+ position.coords.longitude
		//latlong =google.maps.LatLng(position.coords.latitude,position.coords.longitude);
		map = $("#map").gmap({'center':latlong, 'zoom':15});
		$('#map').gmap('addMarker', {'position':position.coords.latitude+','+position.coords.longitude});
	});	
	/*db.transaction(function (tx) {
		tx.executeSql('SELECT * FROM ZPHOTO', [], function (tx, results) {
	  		var len = results.rows.length, i;
	  		for (i = 0; i < len; i++) {
	    			$('#map').gmap('addMarker', {'position':results.rows.item(i).ZCOORD_LAT+','+results.rows.item(i).ZCOORD_LONG}).click((function(index){return function()  {
                			$('#map').gmap('openInfoWindow', { 'content': results.rows.item(index).ZDESC + "<img src='images/"+results.rows.item(index).ZCONTENTDMNUMBER+".jpg' style='width:100px;height:100px;'></img>" }, this);
        			}})(i)); 
	  		}
		});
	});*/
	var len = data.length;
	for (i = 0; i < len; i++) {
		$('#map').gmap('addMarker', {'position':data[i].LAT+','+data[i].LONG}).click((function(index){return function()  {
        		$('#map').gmap('openInfoWindow', { 'content': data[index].VISUAL_DESCRIPTION + "<img src='images/"+data[index].CONTENTDM+".jpg' style='width:100px;height:100px;'></img>" }, this);
        	}})(i)); 
	}
}	
