
var extraImagesCount = 0;


$(document).ready(function () {

    $('input[name="extraImage"]').each(function (index) {

        extraImagesCount++;
        $(this).change(function () {
            if (checkFileSize(this)) {
                showImage(this, index)
            }
            //showImage(this, index)
        })

    })

    $('a[name="linkRemoveExtra"]').each(function (index) {
        
        $(this).click(function () {
            removeExtraImage(index)
        })
    })


});



function showImage(file, index) {

    var f = file.files[0]

    fileName = f.name

    imageNameHiddenField = $('#imageName' + index)
    if (imageNameHiddenField.length) {
        imageNameHiddenField.val(fileName)
    }
    var reader = new FileReader()

    reader.onload = function (e) {
        $('#extraThumbnail' + (index)).attr('src', e.target.result)

    }
    reader.readAsDataURL(f)
    if (index >= extraImagesCount - 1) {
        addExtraImageSection(index + 1)
    }


}

function addExtraImageSection(index) {


    html = ` <div class="col border m-3 p-2" id="divExtraImage${index}">
                <div id="extraImageHeader${index}">
                    <label for="">Extra Images ${index + 1}</label>
                </div>
                <div class="m-2">
                    <img id="extraThumbnail${index}" src="${defaultImageThumbnail}" alt="extraThumbnail${index + 1}"
                        class="img-fluid">
                </div>
                <div>
                    <input accept="image/png, image/jpeg" type="file" id="extraImage${index}" name="extraImage" 
                        onchange="showImage(this,${index})" />
                </div>
            </div>`


    htmlinkRemove = `<a class="btn fas fa-times-circle icon-dark float-right fa-2x" title="Remove this Image"
                        href="javascript:removeExtraImage(${index - 1})"> </a>`


    $('#divProductImages').append(html)
    $(`#extraImageHeader${index - 1}`).append(htmlinkRemove)

    extraImagesCount++;


}

function removeExtraImage(index) {

    $('#divExtraImage' + index).remove()

}

