// this is now obsolete, scripts should now use the data variable.
/*$(function() {
  	var db = openDatabase('myDatabase', '1.0', 'myDatabase', 10000000);
	db.transaction(function (tx) {
		tx.executeSql('SELECT Z_VERSION FROM Z_METADATA', [], function (tx, results) {
			console.log(results);
			if(results.rows.length>0){
				console.log('already loaded');
			}

		},function(e){
			console.log('loading')
			$.ajax('./data/myDatabase.sql', {
				success:function(response) {
	  				// console.log("got db dump!", response);
	  				processQuery(db, 2, response.split(';\n'), 'myDatabase')
				}
			});
		});
	});
});
function processQuery(db, i, queries, dbname) {
    if(i < queries.length -1) {
      console.log(i +' of '+queries.length);
      if(!queries[i+1].match(/(INSERT|CREATE|DROP|PRAGMA|BEGIN|COMMIT)/)) {
        queries[i+1] = queries[i]+ ';\n' + queries[i+1];
         return processQuery(db, i+1, queries, dbname);
      }
     console.log('------------>', queries[i]);
      db.transaction( function (query){ 
        query.executeSql(queries[i]+';', [], function(tx, result) {
          processQuery(db, i +1, queries,dbname);  
        });          
      }, function(err) { 
      console.log("Query error in ", queries[i], err.message);                          
      processQuery(db, i +1, queries, dbname);   
      });
  } else {
      console.log("Done importing!");
  }
}

*/
