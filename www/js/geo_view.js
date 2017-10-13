function initMap() {

    var map = new google.maps.Map(document.getElementById('geo-map'), {
      zoom: 2,
      center: {lat: 0, lng: 0}
    });

    // Create an array of alphabetical characters used to label the markers.
    var labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

    // Add some markers to the map.
    // Note: The code uses the JavaScript Array.prototype.map() method to
    // create an array of markers based on a given "locations" array.
    // The map() method here has nothing to do with the Google Maps API.

    var markersArray = [];  
    var markers = [];
    var images = [];

    for (i = 0; i < locations.length; i++) {  
        images[i] = {
            url: locations[i].url,
            scaledSize: new google.maps.Size(100, 100)
        }

        marker = new google.maps.Marker({
            position: new google.maps.LatLng(locations[i].lat, locations[i].lng),
            map: map,
            hovericon: images[i], 
            icon: 'http://maps.google.com/mapfiles/ms/icons/red-dot.png'
        });

        google.maps.event.addListener(marker, "mouseover", function() {
           this.setIcon(this.hovericon);
        });
       
        google.maps.event.addListener(marker, "mouseout", function() {
           this.setIcon('http://maps.google.com/mapfiles/ms/icons/red-dot.png');
        });
        
        markers.push(marker);
    }


    // Add a marker clusterer to manage the markers.
    var markerCluster = new MarkerClusterer(map, markers,
        {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});
}

function filter_images(){
    values = $("#range-filter").val()
    values = values.split(",");
    min = values[0];
    max = values[1];
    window.location.href = curr_loc+"&min="+min+"&max="+max;
}

function get_display_date(diff){
    abs = 365 - diff;

    options = {  
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    };
    var d = new Date();
    d.setDate(d.getDate()-abs);
    return d.toLocaleString('en-us', options);
}

$(function() {
    $("#current-range-min").text(get_display_date(min_date) );
    $("#current-range-max").text(get_display_date(max_date) );
    
    $("#range-filter").bootstrapSlider({
        
    });
    $("#range-filter").on("slide", function(slideEvt) {
        $("#current-range-min").text(get_display_date(slideEvt.value[0]) );
        $("#current-range-max").text(get_display_date(slideEvt.value[1]) ) ;
    });
    
});




