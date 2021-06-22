function showBtn() {
    $('.carousel-control').removeAttr('hidden')
}

function hiddenBtn() {
    $('.carousel-control').attr('hidden', 'hidden')
}

function inc(){
   var curr =  $(".cart-plus-minus-box").val()
    var newV = parseInt(curr) + 1;
    newV = Math.min(newV, 0)
   $(".cart-plus-minus-box").val(parseInt(curr) + 1)
}

function dec(){
    var curr =  $(".cart-plus-minus-box").val()
    var newV = parseInt(curr) - 1;
    newV = Math.max(newV, parseInt($('#quantity').val()));
    $(".cart-plus-minus-box").val(newV)
}

function descr(){
    if($('.rev').hasClass('active')){
        $('.rev').removeClass('active')
    }

    if(!$('.descr').hasClass('active')){
        $('.descr').addClass('active')
    }

    $('#rev').attr('hidden', 'hidden');
    $('#descr').removeAttr('hidden');
}


function reviews(){
    if(!$('.rev').hasClass('active')){
        $('.rev').addClass('active')
    }

    if($('.descr').hasClass('active')){
        $('.descr').removeClass('active')
    }


    $('#descr').attr('hidden', 'hidden');
    $('#rev').removeAttr('hidden');
}
