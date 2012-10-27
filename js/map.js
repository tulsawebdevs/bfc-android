function loadmap(){
  var map = $('#map');
  navigator.geolocation.getCurrentPosition(function(position) {
    //alert('itworked');
    latlong = position.coords.latitude+','+ position.coords.longitude
    //latlong =google.maps.LatLng(position.coords.latitude,position.coords.longitude);
    map = map.gmap({'center':latlong, 'zoom':15});
    //$('#map').gmap('addMarker', {'position':position.coords.latitude+','+position.coords.longitude});
  });

  if(map.gmap('get','markers').length != data.length){
    map.gmap('clear','markers');
    var len = data.length;
    for (i = 0; i < len; i++) {
      map.gmap('addMarker', {'position':data[i].LAT+','+data[i].LONG}).click((function(index){return function()  {
        map.gmap('openInfoWindow', { 'content': data[index].VISUAL_DESCRIPTION + "<img src='images/"+data[index].CONTENTDM+".jpg' style='width:100px;height:100px;'></img>" }, this);
      }})(i)); 
    }
  }
}
