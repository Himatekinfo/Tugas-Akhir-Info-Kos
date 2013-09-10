<!DOCTYPE html>
<html>
    <head>
        <title>Eto::Polisi</title>
        <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
        <meta charset="utf-8">
        <style>
            html, body, #map-canvas {
                margin: 0;
                padding: 0;
                height: 640px;
            }
        </style>
        <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
        <script>
            var map;
            function initialize() {
                var mapOptions = {
                    zoom: 13,
                    center: new google.maps.LatLng(-6.594015, 106.799759),
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                map = new google.maps.Map(document.getElementById('map-canvas'),
                        mapOptions);

<?php $i = 0; ?>
<?php foreach ($model as $data) { ?>
    <?php $model2 = IncidentImage::model()->findAll("IncidentId='" . $data->Id . "'"); ?>
                    var contentString<?php echo $i; ?> = '<div id="content" style="width:300px; height: 200px"><h3><?php echo $data->Type ?></h3>' +
                            '<table>' +
                            '<tr><td>Waktu:</td><td><?php echo $data->IncidentTime ?></td></tr>' +
                            '<tr><td>Lokasi:</td><td><?php echo $data->Location ?></td></tr>' +
                            '<tr><td>Pelaku:</td><td><?php echo $data->Culprit ?></td></tr>' +
                            '<tr><td>Korban:</td><td><?php echo $data->Victim ?></td></tr>' +
                            '<tr><td>Kerugian:</td><td><?php echo $data->LossValue ?></td></tr>' +
                            '<tr><td>Keterangan:</td><td><?php echo $data->Description ?></td></tr>' +
                            '</table>' +
    <?php foreach ($model2 as $data2) { ?>
                        '<a href="<?php echo $data2->FilePath ?>" target="_blank" ><img src="<?php echo $data2->FilePath ?>" style="width:120px; margin-bottom:15px" /></a>' +
    <?php } ?>
                    '</div>';

                    var infowindow<?php echo $i; ?> = new google.maps.InfoWindow({
                        content: contentString<?php echo $i; ?>
                    });


                    var marker<?php echo $i; ?> = new google.maps.Marker({
                        position: new google.maps.LatLng(<?php echo $data->Latitude; ?>, <?php echo $data->Longitude; ?>),
                        map: map,
                        title: '<?php echo $data->Description ?>'
                    });

                    google.maps.event.addListener(marker<?php echo $i; ?>, 'click', function() {
                        infowindow<?php echo $i; ?>.open(map, marker<?php echo $i; ?>);
                    });

    <?php $i++; ?>
<?php } ?>
            }

            google.maps.event.addDomListener(window, 'load', initialize);

        </script>
    </head>
    <body>
        <div id="map-canvas"></div>
    </body>
</html>