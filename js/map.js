function loadmap(){
  $('#map').gmap().bind('init',function(event,map){
    navigator.geolocation.getCurrentPosition(function(position) {
      //alert('itworked');
      var latlong = new google.maps.LatLng(position.coords.latitude, position.coords.longitude,true);
      $('#map').gmap('option','center',latlong);
      $('#map').gmap('option','zoom',15);
      $('#map').gmap('refresh');
    });

    if($('#map').gmap('get','markers').length != data.length){
      $('#map').gmap('clear','markers');
      var len = data.length;
      for (i = 0; i < len; i++) {
        $('#map').gmap('addMarker',{'position':data[i].LAT+','+data[i].LONG}).click((function(index){return function()  {
          $('#map').gmap('openInfoWindow',{ 'content': data[index].VISUAL_DESCRIPTION + "<img src='images/"+data[index].CONTENTDM+".jpg' style='width:100px;height:100px;'></img>" }, this);
        }})(i)); 
      }
    }
  });
}
