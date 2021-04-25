function checkPage(str) {
    if (str === 'prev') {
        const prev = document.getElementById("prev");
        if (prev.getAttribute("value") === "0") {
            alert("이전 페이지 목록이 없습니다 .");
            prev.setAttribute('disable', "true");
        } else {
            window.location.href = "/host/?page=" + prev.value;
        }
    } else {
        const next = document.getElementById("next");
        if (next.getAttribute("value") === "0") {
            alert("다음 페이지 목록이 없습니다 .");
            next.setAttribute('disable', "true");
        } else {
            window.location.href = "/host/?page=" + next.value;
        }
    }
}

function submit() {
    const sortAsc = document.getElementById('sortAsc');
    $.ajax({
        url: '/search/host',
        type: 'post',
        param: {sortAsc: sortAsc, searchType: "sort"},
        success: function (response) {
            console.log(response);
            $('#sortAsc').val(sortAsc);
        }
    });
}