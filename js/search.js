$(function(){
  $('#search-form').submit(function(event){
    $('#search-results').empty();
    db.transaction(function (tx) {
      var query="%"+$('#search-basic').val()+"%";
      tx.executeSql('SELECT * FROM ZPHOTO WHERE ZDESC LIKE ?;',[query], function (tx, results){
        navigator.geolocation.getCurrentPosition(
          function(position){
            var lat = position.coords.latitude;
            var lng = position.coords.longitude;
            var len = results.rows.length,i;
            var data=[]
            for (i=0;i<len;i++){
              var d = results.rows.item(i);
              d.distance = distance(d.ZCOORD_LAT,d.ZCOORD_LONG,lat,lng);
              if(d.distance<0){
                d.distance_format = (d.distance*1000).toFixed(1) + "m";
              }else{
                d.distance_format = d.distance.toFixed(2) + "km";
              }
              data.push(d);
            }
            listViewTemplate("#search-results","#Search div #photopopup img",data,"listitem.html");
          });
      });
    });
    return false;
  });
  $( "#photopopup" ).on({
        popupbeforeposition: function() {
            var maxHeight = $( window ).height() - 60 + "px";
            $( "#photopopup img" ).css( "max-height", maxHeight );
        }
    });
});
function popupImage(idNum){
  $( "#photopopup img" ).attr('src','images/'+idNum+'.jpg');
}


