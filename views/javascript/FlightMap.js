//TODO: Document each property
//TODO: Document each function

function displayError(message, details) {
   // Display the error message to the user
   console.error(message, details);

   // You can choose to show the error message in the HTML or use other UI elements
   var errorElement = document.createElement('div');
   errorElement.style.color = 'red';
   errorElement.innerHTML = `<strong>Error:</strong> ${message}<br>${details}`;
   const section = document.getElementById('buttons-hbox');
   section.appendChild(errorElement);
 }

function createFlights(data) {
    //convert JSON data to string
    data = JSON.stringify(data);
    //create a dictionary from the string
    let dict = JSON.parse(data);
    try{
        window.myLayerGroup.clearLayers();

        for (var key in dict) {
            window.flightList[key] = [];
            console.log(dict[key]);
            var marker1 = L.marker(dict[key]["arrAirportLocation"]).addTo(window.myLayerGroup);
            var marker2 = L.marker(dict[key]["depAirportLocation"]).addTo(window.myLayerGroup);

            var popup1 = L.popup()
                    .setLatLng(dict[key]["arrAirportLocation"])
                    .setContent(dict[key]["arrAirportDetail"])
                    .openOn(window.myLayerGroup);

            var popup2 = L.popup()
                    .setLatLng(dict[key]["depAirportLocation"])
                    .setContent(dict[key]["depAirportDetail"])
                    .openOn(window.myLayerGroup);

            marker1.bindPopup(dict[key]["arrAirportDetail"]).openPopup();
            marker2.bindPopup(dict[key]["depAirportDetail"]).openPopup();

            var polyline = L.polygon([
              dict[key]["arrAirportLocation"],
              dict[key]["depAirportLocation"],
            ], {color: 'blue', weight: 10}).addTo(window.myLayerGroup);

            polyline.on('click',window.onPolyClick);

            var arrdetails = dict[key]["ShortDetails"];
            arrdetails["depAirportDetail"] = dict[key]["depAirportDetail"];

            window.flightList[key].push(marker1);//0
            window.flightList[key].push(marker2);//1
            window.flightList[key].push(popup1);//2
            window.flightList[key].push(popup2);//3
            window.flightList[key].push(polyline);//4
            window.flightList[key].push(10);//5
            window.flightList[key].push(dict[key]["flightDetail"]);//7
            window.flightList[key].push(arrdetails);//6
        }
    } catch (error) {
        // Handle the error
        throw error;
        window.displayError('error creating a flight: ', error.message);
    }
}

function lineThickness(operation) {
    //Increase or decrease the line thickness
    let flightList = window.flightList;
    for (var key in window.flightList) {
        if (operation === "+" && window.flightList[key][5] < 40) {
            window.flightList[key][5] += 2;
            window.flightList[key][4].setStyle({weight: flightList[key][5] });
        } else if (operation === "-" && window.flightList[key][5] > 2) {
            window.flightList[key][5] -= 2;
            window.flightList[key][4].setStyle({weight: flightList[key][5] });
        }
    }
}

function refocus() {
    window.map.setView([31, 45], 2);
}

// Function to apply colorblind filter to the map
function applyColorblindFilter(filterName) {
    setTimeout(() => {
        const filterValue = filterName === "none" ? "" : `url(${filterName})`;
        //select map
        const mapElement = document.querySelector("map");
        // Update the filter
        mapElement.style.filter = filterValue;
    }, 50); // Delay of 50ms
}

function onPolyClick(e) {
    console.log("polyline clicked");

    //rewrite the loop using for i in range
    for (let flight_id in window.flightList) {
    let flight = window.flightList[flight_id];
    console.log(flight);

        if (flight[4] === e.target) {
            $('myModalSent').modal('show');
            $('modal-title').text(flight[7]);
            var dict = flight[6];

            for (const key in dict) {
                if (key === "arrAirport") {
                    $('arrAirport').text("Arrival Airport: " + dict[key]);
                } else if (key === "status") {
                    $('status').text(key+ ": " + dict[key]);
                } else if (key === "depAirport") {
                    $('depAirport').text("Departure Airport: " + dict[key]);
                } else if (key === "date") {
                    $('date').text("Date: " + dict[key]);
                }
            }
            break;
        }
    }
}

function mapInit(){
   // Event listener for the dropdown
   document.getElementById('filterDropdown').addEventListener('change', (event) => {
       window.applyColorblindFilter(event.target.value);
   });
}


window.onload = ()=> {
    //Set the global variables
    window.osm = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap'
    });

    window.osmHOT = L.tileLayer('https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap contributors, Tiles style by Humanitarian OpenStreetMap Team hosted by OpenStreetMap France'});

    window.openTopoMap = L.tileLayer('https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: 'Map data: © OpenStreetMap contributors, SRTM | Map style: © OpenTopoMap (CC-BY-SA)'
    });

    window.map = L.map('map', {
      center: [39.73, -104.99],
      zoom: 10,
      layers: [osm] //cities]
    }).setView([51.505, -1.09], 10);

    window.mapLineSize = 10;

    window.baseMaps = {
      "Open Street Map": window.osm,
      "<span style='color: red'>Open Street Map.HOT</span>": window.osmHOT,
      "<span style='color: blue'>Topographical Map</span>": window.openTopoMap
    };

    window.layerControl = L.control.layers(window.baseMaps).addTo(window.map); //overlayMaps).addTo(map);

    window.myLayerGroup = L.layerGroup().addTo(window.map);

    window.flightList = {};

    //initialize the map and the event listeners
    mapInit();
};
