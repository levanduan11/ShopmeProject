
dropdowBrand = $('#brand')
dropdowCategories = $('#category')


$(document).ready(function () {

    $('#shortDescription').richText()
    $('#fullDescription').richText()
    dropdowBrand.change(function () {
        dropdowCategories.empty()
        getCategories()

    });
    getCategoriesForNewForm()

});

 function getCategoriesForNewForm(){
     catId = $('#categoryId')
     editMode = false;
     if (catId.length) {
         editMode = true;
     }
    
     if (!editMode) {
         getCategories()
     }
}

function getCategories() {
    brandId = dropdowBrand.val();
    url = brandModuleURL + "/" + brandId + "/categories"
    $.get(url,
        function (responJson) {
            $.each(responJson, function (index, category) {
                $("<option>").val(category.id).text(category.name).appendTo(dropdowCategories)
            });
        }
    );


}

function checkUniqueName(form) {

    nameValue = $('#name').val()
    productId = $('#id').val()
    csrfValue = $('input[name="_csrf"]').val()

    param = {
        id: productId,
        name: nameValue,
        _csrf: csrfValue
    }
    $.post(checkUniqueUrl, param,
        function (response) {
            if (response == true) {
                form.submit()
            } else if (response == false) {
                showWarningModal("duplicate name " + nameValue)
            } else {
                showErrorModal("Unknow response from server")
            }
        }

    ).fail(function () {
        showModal('Error', "Could not connect to the server")
    });


    return false;

}


