function loadmap()
	{
	navigator.geolocation.getCurrentPosition(function(position) {
		//alert('itworked');
		latlong = position.coords.latitude+','+ position.coords.longitude
		//latlong =google.maps.LatLng(position.coords.latitude,position.coords.longitude);
		map = $("#map").gmap({'center':latlong})
	});	
}
