

$(document).ready( function() {

    lightbox.option({
      'resizeDuration': 200,
      'wrapAround': true
    });
    
});

Dropzone.autoDiscover = false;

var myDropzone = new Dropzone("#form-upload", {
    paramName: "file", // The name that will be used to transfer the file
    maxFilesize: 2, // MB
    uploadMultiple : true, 
    parallelUploads: 0,
    autoProcessQueue: false,
    acceptedFiles: 'image/*'
});

myDropzone.on("addedfile", function(file) { 
    $.ajax({
        url: '/generate_upload_url',
        async: false,
        success: function(data) {
            $("#form-upload").attr('action', data);
            myDropzone.options.url = data;
        }
    });
});

function startUpload(){
    for (var i = 0; i < myDropzone.getAcceptedFiles().length; i++) {
        myDropzone.processFile(myDropzone.getAcceptedFiles()[i]);
    }
    location.reload();
}

function show(elem){
    $(elem).fadeIn();
}