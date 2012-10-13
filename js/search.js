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
                  "<li>"+item.ZTITLE+" "+
                  item.ZDESC+" "+
                  "</li>"
                );
              }
              $('#search-results').listview('refresh');
			});
		});
		return false;
	});
});


