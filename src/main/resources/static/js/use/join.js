// 비밀 번호 인자 중복 검사 로직
function noDuple(str) {
    let strArray = [];
    for (let i = 0; i < str.length; i++) {
        strArray.push(str.charAt(i));
    }
    const strSetArray = Array.from(new Set(strArray));
    return JSON.stringify(strArray) === JSON.stringify(strSetArray);
}

// 유효성 검사
function checkInfo() {
    const idMatch = /^[a-zA-Z]{1,10}$/; // 아이디 유효성 검사
    const pwdMatch = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/; // 비밀번호 유효성 검사

    const userId = document.getElementById("userId").value;
    const pwd = document.getElementById("pwd").value;
    const pwd2 = document.getElementById("pwd2").value;
    const userName = document.getElementById("userName").value;

    const infoList = [userId, pwd, pwd2, userName];

    // 비어 있는 값 검사 (userName 빈값 방지)
    for (let info in infoList) {
        if (info === "") {
            alert("비어 있는 값이 있습니다 .");
            return false;
        }
    }

    if (!idMatch.test(userId)) { // ID 유효성 검사
        alert("ID 형식에 맞게 입력해 주십시오 .");
        $("#userId").val("").focus();
        return false;
    } else if (!pwdMatch.test(pwd) || !noDuple(pwd)) { // 비밀 번호 유효성 검사
        alert("비밀 번호 형식 맞게 입력해 주십시오 .");
        $("#pwd").val("").focus();
        return false;
    } else if (pwd !== pwd2) { // 비밀 번호와 비밀 번호 확인 검사
        alert("비밀 번호 확인이 비밀 번호와 같지 않습니다 .");
        $("#pw2").val("").focus();
        return false;
    } else if (userName.length < 1) {
        alert("이름 값을 입력해 주십시오 .");
        $("#userName").val("").focus();
        return false;
    }
    return true;
}

function register() {
    const userId = document.getElementById("userId").value;
    if (checkInfo()) {
        $.ajax({
            url: '/api/check/id?userId=' + userId,
            type: 'get',
            dataType: 'json',
            success: function (response) {
                if (response.result === 0) {
                    alert("아이디 중복");
                } else {
                    $('form').attr('action', '/register').attr('method',
                        'post').submit();
                    alert("가입이 완료 되었습니다. \n로그인 해주십시오.");
                }
            },
            fail: function (error) {
                alert("등록에 실패 하였습니다 .");
                console.log(error);
            },
        });
    }
}
