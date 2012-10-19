$(function(){
	/*$('#search-form').submit(function(event){
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
	});*/
	$('#search-form').submit(function(event){
		$('#search-results').empty();
		var query=$('#search-basic').val();
		for (i=0;i<data.length;i++){
			if( (data[i].VISUAL_DESCRIPTION.search(new RegExp(query, "i"))>0) || (data[i].ADDRESS.search(new RegExp(query, "i"))>0)){
				$('#search-results').append("<li><a href='#photopopup2' data-rel='popup' onClick='popupImage2("+data[i].CONTENTDM+")'>"+data[i].ADDRESS+" "+data[i].VISUAL_DESCRIPTION+" "+"</a></li>");
			}
		}
		$('#search-results').listview('refresh');
		return false;
	});
	$( "#photopopup2" ).on({
        popupbeforeposition: resizepopup2
    });
});
function popupImage2(idNum){
	$( "#photopopup2 img" ).attr('src','images/'+idNum+'.jpg');
	resizepopup2()
}
function resizepopup2() {
	var maxHeight = $( window ).height() - 60 + "px";
	$( "#photopopup2 img" ).css( "max-height", maxHeight );
}

