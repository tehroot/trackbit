$.fn.loadMap = function(){
    //url location
    var coordinateURL = "http://localhost:6555/polyline"
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

$.fn.register = function(){
    $("form").on("submit", function(event){
        event.preventDefault();
        var fields = $(this).serializeArray();
        var data = {};
        //strip the serialization artifacts into a new field array
        //the length is kind of irrelevant
        $(fields).each(function(index, obj){
            data[obj.name] = obj.value;
        });
        if(fields.length == 2){
            $.ajax({
                type: "post",
                url: "http://localhost:6555/register",
                contentType: "application/json",
                dataType: "text",
                data: JSON.stringify(data),
            success: function(response){
                //redirect from here to index with user session???
                alert("Posted");
            },
            error: function(ajaxRequest, statusText, errorMessage){
                alert("Error: " +ajaxRequest.responseText +"\n" + statusText + "\n" +errorMessage);
            }
            });
        this.reset();
        }
    });
}

$.fn.login = function(){
    $("form").on("submit", function(event){
        event.preventDefault();
        var fields = $(this).serializeArray();
        var data = {};
        //strip the serialization artifacts into a new field array
        //the length is kind of irrelevant
        $(fields).each(function(index, obj){
            data[obj.name] = obj.value;
        });
        console.log(data);
        if(fields.length == 2 || fields.length == 3){
            $.ajax({
                type: "post",
                url: "http://localhost:6555/login",
                contentType: "application/json",
                dataType: "text",
                data: JSON.stringify(data),
            success: function(response){
                //redirect from here to index with user session???
                alert("Posted");
            },
            error: function(ajaxRequest, statusText, errorMessage){
                if(ajaxRequest.status == 401){
                    $("#invalidLogin").css("visibility", "visible");
                    $("#invalidLogin").text("Invalid Login");
                }
            }
            });
            
        this.reset();
        }
    });
}

