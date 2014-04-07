$(document).ready(function(){
    var _items = $('td[title="status"]');
    for( i=0; i<_items.length; i++ ){
            var _book_status = _items[i].innerHTML;
            var _book__isbn = _items[i].id.slice("6");
            var _temp = "#"+_book__isbn;
            if(_book_status == "lost")
                    {
                            $(_temp).attr("disabled","disabled");
                    }
            else{
                    $(_temp).removeAttr("disabled");
            }
    }
    
});


$(":button").click(function() {
	var _isbn = this.id;
	alert('About to report lost on _isbn ' + _isbn);
	_callee(_isbn);
	$("#"+_isbn).attr("disabled", "disabled");
	window.location.reload(true);
});



function _callee(_isbn)
{
	//alert(_isbn);
	$.ajax({
	    type: "PUT",
	    url: "/library/v1/books/"+_isbn+"/?status=lost",
	    contentType: "application/json",
	    success: function() {
            window.location.reload();
	    },
	    error: function() {
            window.location.relaod();
    }
	});
	$(status).text("lost")
	
	
}