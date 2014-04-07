 $(document).ready(function(){
    var elements = $('td[title="status"]');
    for( i=0; i<elements.length; i++ ){
            var statusIs = elements[i].innerHTML;
            var onlyNumberId = elements[i].id.slice("6");
            var realId = "#"+onlyNumberId;
            if(statusIs == "lost")
                    {
                            $(realId).attr("disabled","disabled");
                            //window.location.reload();
                    }
            else{
                    $(realId).removeAttr("disabled");
                    
            }
    }
});
 