
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
  var db = openDatabase('myDatabase','1.0','myDatabase',10000000);
  db.transaction(function(tx){
    tx.executeSql('select ZCONTENTDMNUMBER, ZCOORD_LAT as lat, ZCOORD_LONG as lng, ZTITLE, ZDESC from ZPHOTO',[], 
    function(tx,results){
      var len = results.rows.length,i;
      var data=[]
      for (i=0;i<len;i++){
        var d = results.rows.item(i);
        d.distance = distance(d.lat,d.lng,lat,lng);
        if(d.distance<0){
          d.distance_format = (d.distance*1000).toFixed(1) + "m";
        }else{
          d.distance_format = d.distance.toFixed(2) + "km";
        }
        data.push(d);
        //callback(d);
      }
      data=_.sortBy(data,function(d){return d.distance;}); 
      callback(data); 
    });
  });
}

function populateNearYou(tag,imageTag){
  navigator.geolocation.getCurrentPosition(
    function(position){
      placesnear(position.coords.latitude,position.coords.longitude,
        function(data){
          //var i;
          console.log("Length: "+data.length);
          listViewTemplate(tag,imageTag,data,"listitem.html");
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
