
$(document).ready(function () {

    $('a[name="linkRemoveDetail"]').each(function (index) {
        console.log(this)
        $(this).click(function () {
            removeByIndex(index)
        })

    })


});


function removeByIndex(index) {
    
    $('#divDetails'+index).remove()
}

function addNextDetails() {

    allDivDetails = $("[id^='divDetails']")
    divDetailsCount = allDivDetails.length



    html = ` <div class="form-row" id="divDetails${divDetailsCount}">
     <input type="hidden" name="detailsId" value="0">
            <div class="form-group col-md-4">
                <label>Name</label>
                <input type="text" class="form-control" placeholder="name" name="detailNames">
            </div>
            <div class="form-group col-md-4">
                <label>Value</label>
                <input type="text" class="form-control" placeholder="value" name="detailValues">
            </div>
        </div>`



    previousDivDetails = allDivDetails.last()
    previousDivDetailsId = previousDivDetails.attr("id")

    $('#divProductDetails').append(html)
    htmlinkRemove = `<a class="btn fas fa-times-circle icon-dark float-right fa-2x" title="Remove this details"
                        href="javascript:removeId('${previousDivDetailsId}')"> </a>`


    console.log(previousDivDetails)
    previousDivDetails.append(htmlinkRemove)

    $('input[name="detailNames"]').last().focus()

}

function removeId(id) {

    $('#' + id).remove()

}