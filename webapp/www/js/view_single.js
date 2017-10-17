

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

myDropzone.on("complete", function() {
  // If all files have been uploaded
  if (this.getQueuedFiles().length == 0 && this.getUploadingFiles().length == 0) {
    completedQueue();
  }
});

myDropzone.on("addedfile", function(file) { 
    $("#start-upload").show()
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
    $("#start-upload").hide()
    for (var i = 0; i < myDropzone.getAcceptedFiles().length; i++) {
        $.ajax({
            url: '/generate_upload_url',
            async: false,
            success: function(data) {
                $("#form-upload").attr('action', data);
                myDropzone.options.url = data;
            }
        });
        myDropzone.processFile(myDropzone.getAcceptedFiles()[i]);
    }
}

function completedQueue(){
    $("#more-upload").show()
    $(".dropzone").addClass("disabled");
    myDropzone.disable()
}

function restartUpload(){
    myDropzone.enable();
    $(".dropzone").removeClass("disabled");
    myDropzone.removeAllFiles();
    $("#more-upload").hide();
}

function reloadPage(){
    window.location.reload;
}

function show(elem){
    $(elem).fadeIn();
}