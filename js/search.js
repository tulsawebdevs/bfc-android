$(function(){
  $('#search-form').submit(function(event){
    $('#search-results').empty();
    navigator.geolocation.getCurrentPosition(
      function(position){
        var lat = position.coords.latitude;
        var lng = position.coords.longitude;
        var srch = $('#search-basic').val().toLowerCase();
        var data1 = _.filter(data,function(item){
          return item.NAME.toLowerCase().indexOf(srch) != -1 ||
                 item.VISUAL_DESCRIPTION.toLowerCase().indexOf(srch) !=-1 ||
                 item.ADDRESS.toLowerCase().indexOf(srch) != -1 ||
                 item.TAGS.toLowerCase().indexOf(srch) !=-1;
        });
        var len = data1.length;
        for (i=0;i<len;i++){
          var d = data1[i];
          d.distance = distance(d.LAT,d.LONG,lat,lng);
          if(d.distance<0){
            d.distance_format = (d.distance*1000).toFixed(1) + "m";
          }else{
            d.distance_format = d.distance.toFixed(2) + "km";
          }
          data1[i]=d;
        }
        data = _.sortBy(data,function(d){return d.distance});
        listViewTemplate("#search-results","#Search div #photopopup img",data1,"listitem.html");
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
  resizepopup();
}
function resizepopup() {
  var maxHeight = $( window ).height() - 60 + "px";
  $( "#photopopup img" ).css( "max-height", maxHeight );
}


