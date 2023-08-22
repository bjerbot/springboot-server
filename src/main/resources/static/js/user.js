//取得sessionStorage資訊
let user = JSON.parse(sessionStorage.getItem("user"));
let token = sessionStorage.getItem("token");
console.log(user);
console.log(token);

if(user != null || token != null){
    window.location.href = "../index.html";
}

// 轉跳到註冊頁面
let jumpToRegisterBtn = document.querySelector("a#jumpToRegister");

jumpToRegisterBtn.addEventListener("click", e => {
    e.preventDefault();

    showRegisterForm();
})

//登入功能
let loginSubmit = document.querySelector("input#login_submit");

loginSubmit.addEventListener("click", e => {
    e.preventDefault();

    let email = document.querySelector("input#login_email").value;
    let password = document.querySelector("input#login_password").value;

    let body = {
        email,
        password
    }

    let url = "http://localhost:8080/users/login";

    fetch(url, {
        method:"POST",
        headers:{
            "Content-Type" : "application/json"
        },
        body:JSON.stringify(body)
    }).then(response => {
            if(response.status == 400){
            console.log("登入失敗");
            throw new Error("登入失敗");
        }
        return response.json()
    }).then(data => {
        console.log(data);
        let token = data["token"];
        let userObject = data["user"];
        let user = JSON.stringify(userObject);

        console.log(token);
        console.log(user);

        sessionStorage.setItem("token", token);
        sessionStorage.setItem("user", user);
        window.location.href = "../index.html"
    })
});

// 轉跳到登入頁面
let jumpToLoginBtn = document.querySelector("a#jumpToLogin");

jumpToLoginBtn.addEventListener("click", e => {
    e.preventDefault();

    showLoginForm();
})

//註冊功能
let registerSubmit = document.querySelector("input#register_submit");

registerSubmit.addEventListener("click", e => {
    e.preventDefault();

    let email = document.querySelector("input#register_email").value;
    let password = document.querySelector("input#register_password").value;
    let conformPassword = document.querySelector("input#register_conform_password").value;

    if(password == conformPassword) {
        let body = {
            email,
            password
        }

        let url = "http://localhost:8080/users/register";

        fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        }).then(response => {
            console.log(response);

            if(response.status == 200){
                return response.json()
            }
            else if(response.status == 400){
               console.log(response);
                let checkResult = document.querySelector("p#check_result");
                checkResult.innerHTML = "此帳號已被註冊"
                checkResult.style.color = "red";

                let passwordInput = document.querySelector("input#register_password");
                let conformPasswordInput = document.querySelector("input#register_conform_password");

                passwordInput.value = "";
                conformPasswordInput.value = "";

                throw new Error("此帳號已被註冊");
            }
        }).then(data => {
            console.log(data);

            showLoginForm();

        }).catch(error => {
            console.error(error);
            console.error(error.message);
        })
    }
    else{
        let checkResult = document.querySelector("p#check_result");
        checkResult.innerHTML = "密碼不一致，請重新輸入"
        checkResult.style.color = "red";

        let emailInput = document.querySelector("input#register_email");
        let passwordInput = document.querySelector("input#register_password");
        let conformPasswordInput = document.querySelector("input#register_conform_password");

        emailInput.value = "";
        passwordInput.value = "";
        conformPasswordInput.value = "";
    }
});

//隱藏註冊表格 & 顯示登入表格
function showLoginForm(){
    console.log("showLoginForm");
    let registerForm = document.querySelector("form#register_form");
    let loginForm = document.querySelector("form#login_form");

    registerForm.style.display = "none";
    loginForm.style.display = "inline-block";
}

//隱藏登入表格 & 顯示顯示表格
function showRegisterForm(){
    console.log("showRegisterForm");
    let registerForm = document.querySelector("form#register_form");
    let loginForm = document.querySelector("form#login_form");

    registerForm.style.display = "inline-block";
    loginForm.style.display = "none";
}