// 轉跳到註冊頁面
let jumpToRegisterBtn = document.querySelector("a#jumpToRegister");

jumpToRegisterBtn.addEventListener("click", e => {
    e.preventDefault();

    console.log("click jumpToRegisterBtn");
    let registerForm = document.querySelector("form#register_form");
    let loginForm = document.querySelector("form#login_form");

    registerForm.style.display = "inline-block";
    loginForm.style.display = "none";
})

//登入
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

    console.log("click jumpToLoginBtn");
    let registerForm = document.querySelector("form#register_form");
    let loginForm = document.querySelector("form#login_form");

    registerForm.style.display = "none";
    loginForm.style.display = "inline-block";
})

//註冊
let registerSubmit = document.querySelector("input#register_submit");

registerSubmit.addEventListener("click", e => {
    e.preventDefault();

    let email = document.querySelector("input#register_email").value;
    let password = document.querySelector("input#register_password").value;

    let body = {
        email,
        password
    }

    let url = "http://localhost:8080/users/register";

    fetch(url, {
        method:"POST",
        headers:{
            "Content-Type" : "application/json"
        },
        body:JSON.stringify(body)
    }).then(response => {
        console.log(response);
        return response.json()
    }).then(data => {
        console.log(data);

    })
});


