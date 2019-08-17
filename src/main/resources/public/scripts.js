<script>
$.fn.loadMap = function(){
    //url location
    var coordinateURL = "http://localhost:8080/polyline"
    var latlngs = new Array();
    $.ajax({
        url: coordinateURL,
        async: false,
        dataType: 'json',
        success: function(data){
            latlngs.push(data.coordinates);
        }
    });
    // initialize the map at base coordinates in array
    var map = L.map('map').setView(latlngs[0][0], 13);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
    console.log(latlngs.length);
    // iterate through coordinates list of nested double arrays
    for(var i = 0; i < latlngs.length; i++){
        var latlng = latlngs[i];
        var polyline = L.polyline(latlng, {color: 'red'}).addTo(map);
    }
    // raster tile layer from openstreetmap{x}{y} params are location data passed from initial map load
    //var polyline = L.polyline(latlngs, {color: 'red'}).addTo(map);
    //map.fitBounds(polyline.getBounds());
    // load a tile layer
}

$.fn.login = function(){
    $("form").on("submit", function(event){
        event.preventDefault();
        var fields = $(this).serializeArray();
        if(fields.length == 2){

        }
        $.ajax({
                type: "post",
                url:"https://markcardish.net:6555/register"
                contentType: "application/json",
                dataType: "text",
                data: json,
            success: function(response){
                alert("Posted");
            },
            error: function(ajaxRequest, statusText, errorMessage){
                alert("Error: " +ajaxRequest.responseText +"\n" + statusText + "\n" +errorMessage);
            }
            });
        this.reset();
    });
}
</script>