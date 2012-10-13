
function placesnear(lat,lng,callback){

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

  var db = openDatabase('myDatabase','1.0','myDatabase',10000000);
  db.transaction(function(tx){
    tx.executeSql('select ZCOORD_LAT as lat, ZCOORD_LONG as lng, ZTITLE as title, ZDESC as desc from ZPHOTO',[], 
    function(tx,results){
      var len = results.rows.length,i;
      var data=[]
      for (i=0;i<len;i++){
        var d = results.rows.item(i);
        d.distance = distance(d.lat,d.lng,lat,lng);
        data.push(d);
        //callback(d);
      }
      data=_.sortBy(data,function(d){return d.distance;}); 
      callback(data); 
    });
  });
}


function populateNearYou(tag){
        navigator.geolocation.getCurrentPosition(
        function(position){
          placesnear(position.coords.latitude,position.coords.longitude,
            function(items){
              var i;
              console.log("Length: "+items.length);
              for (i=0;i<items.length;i++){
                item = items[i];
                $(tag).append(
                  "<li>"+item.title+" "+
                  item.distance+" "+
                  "</li>"
                );
              }
              $(tag).listview('refresh');
        })
      })
}
