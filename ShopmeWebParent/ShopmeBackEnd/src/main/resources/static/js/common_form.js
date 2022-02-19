
$(document).ready(function () {
    $('#cancel').on('click', function () {
        window.location = moduleURL;
    });

    $('#fileImage').change(function () {

        // fileSize = this.files[0].size;
        // if (fileSize > MAX_FILE_SIZE) {
        //     this.setCustomValidity("you must choose an image less than " + MAX_FILE_SIZE + "byte")
        //     this.reportValidity();
        // } else {
        //     this.setCustomValidity("")
        //     showImageThumbnail(this)
        // }
        if (checkFileSize(this)) {
            showImageThumbnail(this)
        }


    });


});

function checkFileSize(inputFile) {
    fileSize = inputFile.files[0].size;
    if (fileSize > MAX_FILE_SIZE) {
        inputFile.setCustomValidity("you must choose an image less than " + MAX_FILE_SIZE + "byte")
        inputFile.reportValidity();
        return false;
    }
    inputFile.setCustomValidity("")
    return true;

}

function showImageThumbnail(fileInput) {

    var file = fileInput.files[0]
    var reader = new FileReader()
    reader.onload = function (e) {
        $('#thumbnail').attr('src', e.target.result)

    }
    reader.readAsDataURL(file)


}

function showModal(title, message) {
    $("#exampleModalLabel").text(title);
    $("#modalBody").text(message);
    $("#exampleModal").modal()
}

function showErrorModal(message) {

    showModal("Error", message)
}
function showWarningModal(message) {

    showModal("Warning", message)
}
