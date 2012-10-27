
function distance(lat1,lon1,lat2,lon2) {
  var R = 6371; // km (change this constant to get miles)
  var dLat = (lat2-lat1) * Math.PI / 180;
  var dLon = (lon2-lon1) * Math.PI / 180;
  var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
    Math.cos(lat1 * Math.PI / 180 ) * Math.cos(lat2 * Math.PI / 180 ) *
    Math.sin(dLon/2) * Math.sin(dLon/2);
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
  var d = R * c;
  return d;
}


function placesnear(lat,lng,callback){
  var len = data.length,i;
  var results=[];
  for (i=0;i<len;i++){
    var d = data[i];
    d.distance = distance(d.LAT,d.LONG,lat,lng);
    if(d.distance<0){
      d.distance_format = (d.distance*1000).toFixed(1) + "m";
    }else{
      d.distance_format = d.distance.toFixed(2) + "km";
    }
    results.push(d);
  }
  results=_.sortBy(data,function(d){return d.distance;});
  callback(results); 
}

function populateNearYou(tag,imageTag){
  navigator.geolocation.getCurrentPosition(
    function(position){
      placesnear(position.coords.latitude,position.coords.longitude,
        function(results){
          $.get("listitem.html",function(template) {
            var t = $.mustache(template,{content:results});
            $(tag).append(t);
            $(tag+" ul").listview();
            $( imageTag ).on({
              popupbeforeposition: function() {
                var maxHeight = $( window ).height() - 60 + "px";
                $( imageTag ).css( "max-height", maxHeight );
              }
            });
          });
    })
  })
}

function listViewTemplate(intoTag,imageTag,items,templateUrl){
  $.get(templateUrl,function(template) {
    var t = $.mustache(template,{content:items});
    $(intoTag).append(t);
    $(intoTag+" ul").listview();
    $( imageTag ).on({
      popupbeforeposition: function() {
        var maxHeight = $( window ).height() - 60 + "px";
        $( imageTag ).css( "max-height", maxHeight );
      }
    });
  });
}

$(function(){
    $( "#photopopup" ).on({
        popupbeforeposition: resizepopup
    });
});
function popupImage(idNum){
	$( "#photopopup img" ).attr('src','images/'+idNum+'.jpg');
	resizepopup()
}
function resizepopup() {
	var maxHeight = $( window ).height() - 60 + "px";
	$( "#photopopup img" ).css( "max-height", maxHeight );
}

