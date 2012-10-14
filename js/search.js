$(function(){
	$('#search-form').submit(function(event){
		$('#search-results').empty();
		db.transaction(function (tx) {
			var query="%"+$('#search-basic').val()+"%";
			tx.executeSql('SELECT * FROM ZPHOTO WHERE ZDESC LIKE ?;',[query], function (tx, results){
              var i;
              for (i=0;i<results.rows.length;i++){
                item = results.rows.item(i);
                $('#search-results').append(
                  "<li><a href='#photopopup' data-rel='popup' onClick='popupImage("+item.ZCONTENTDMNUMBER+")'>"+item.ZTITLE+" "+
                  item.ZDESC+" "+"</a></li>"
                );
              }
              $('#search-results').listview('refresh');
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


