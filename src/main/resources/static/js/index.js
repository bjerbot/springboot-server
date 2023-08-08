//shadow animation
let nav=document.querySelector("nav");
window.addEventListener("scroll", ()=>{
    if(window.scrollY == 0){
        nav.style.boxShadow= "";
    }else{
        nav.style.boxShadow="0 10px 6px -6px #777";
    }
})

//向後端驗證token取得登入資訊
let token = sessionStorage.getItem("token");
let userJSONStr = sessionStorage.getItem("user");
let email;

if(userJSONStr != null && token != null && userJSONStr != undefined && token != undefined){
    let user = JSON.parse(userJSONStr);
    email = user.email;
    console.log(token);
    console.log(user);
    console.log(email);

    let url = "http://localhost:8080/validate_login_status";

    let body = {
        email
    }

    fetch(url, {
        method:"POST",
        headers : {
            "Content-Type":"application/json",
            "Authorization":`Bearer ${token}`
        },
        body:JSON.stringify(body)
    })
        .then(response => {
        console.log(response);
        if(response.status == 200){
            console.log(email +"驗證成功");
            userEmailBtn.innerHTML = email;
            userEmailBtn.style.display = "inline"
        }
        else if(response.status == 401){
            console.log("驗證失敗")
        }
        });
}

//使用者菜單的展開和收合
let userEmailBtn = document.querySelector("a#user_email");
let userMenu = document.querySelector("ul#user_menu");

userEmailBtn.addEventListener("click", e => {

    if(userMenu.style.display == "none" || userMenu.style.display == ""){
        userMenu.style.display = "block";
    }
    else if(userMenu.style.display == "block"){
        userMenu.style.display = "none";
    }
})

//登出功能
let signOutBtn = document.querySelector("a#sign_out");

signOutBtn.addEventListener("click", e => {
    console.log("sign out btn");

    sessionStorage.removeItem("token");
    sessionStorage.removeItem("user");

    userMenu.style.display = "none"
    userEmailBtn.style.display = "none"
});


//商品列表相關變數
let currentPage = 1;
const cardCount = 12;
let offset = (currentPage-1)*cardCount;
let category = "all";
let isNextData = true;

//clone商品列表
let cardContainer = document.querySelector("div.card_container");
for(let i = 0; i < cardCount-1; i++) {
    let cloneCard = document.querySelector("div.card").cloneNode(true);

    cardContainer.appendChild(cloneCard);
}

//請求商品列表資料
let categoryAllUrl = "http://localhost:8080/products?limit=" + cardCount + "&offset=" + offset;
queryProducts(categoryAllUrl);

//請求商品列表(按category FOOD)
let categoryFoodBtn = document.querySelector("li#categoryFood");

categoryFoodBtn.addEventListener("click", e => {
    e.preventDefault();

    currentPage = 1;
    // cardCount = 12;
    offset = (currentPage-1)*cardCount;
    category = "FOOD"
    console.log(currentPage);
    console.log(offset);
    let categoryFoodUrl = "http://localhost:8080/products?limit=" + cardCount + "&offset=" + offset + "&category=" + category;

    queryProducts(categoryFoodUrl);

    currentPageBar.innerHTML = currentPage;
})

//請求商品列表(按category CAR)
let categoryCarBtn = document.querySelector("li#categoryCar");

categoryCarBtn.addEventListener("click", e => {
    e.preventDefault();

    currentPage = 1;
    // cardCount = 12;
    offset = (currentPage-1)*cardCount;
    category = "CAR"
    console.log(currentPage);
    console.log(offset);
    let categoryCarUrl = "http://localhost:8080/products?limit=" + cardCount + "&offset=" + offset + "&category=" + category;

    queryProducts(categoryCarUrl);

    currentPageBar.innerHTML = currentPage;
})

//請求商品列表(按分頁)
let Btn_pre = document.querySelector("button#page_previous");
let Btn_next = document.querySelector("button#page_next");

//第一頁時取消上一頁按鈕的點擊事件
if(currentPage == 1){
    Btn_pre.disabled = true;
}

Btn_next.addEventListener("click", async e => {
    e.preventDefault();
    console.log("Btn_next");

    currentPage += 1;
    offset += cardCount;
    console.log(currentPage);
    console.log(offset);
    let pageUrl = "http://localhost:8080/products?limit=" + cardCount + "&offset=" + offset;
    if (category != "all") {
        pageUrl = pageUrl + "&category=" + category;
    }

    isNextData = await queryProducts(pageUrl);

    if (isNextData == false) {
        console.log("not next data");
        Btn_next.disabled = true;
    }

    if(currentPage > 1){
        Btn_pre.disabled = false;
    }

    currentPageBar.innerHTML = currentPage;
})

Btn_pre.addEventListener("click", async e => {
    e.preventDefault();
    console.log("Btn_pre");

    if (currentPage > 1) {
        currentPage -= 1;
        offset -= cardCount;
        console.log(currentPage);
        console.log(offset);
        let pageUrl = "http://localhost:8080/products?limit=" + cardCount + "&offset=" + offset;
        if (category != "all") {
            pageUrl = pageUrl + "&category=" + category;
        }

        isNextData = await queryProducts(pageUrl);

        if (isNextData == true) {
            Btn_next.disabled = false;
        }

        currentPageBar.innerHTML = currentPage;
    }

    if(currentPage == 1){
        Btn_pre.disabled = true;
    }
})

let currentPageBar = document.querySelector("span#current_page");
currentPageBar.innerHTML = currentPage;

async function queryProducts(url) {

    return fetch(url).then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status == 404) {
            console.log("404")
        }
    }).then(data => {
        const limit = Number(data["limit"]);
        const offset = Number(data["offset"]);
        let currentData = data.result.length + offset;
        const totalData = Number(data["total"]);

        console.log("資料筆數:" + data.result.length);
        console.log("limit" + limit);
        console.log("offset" + offset);
        console.log("total" + totalData);
        console.log(currentData);
        console.log(currentData == totalData);

        if(data.result.length > 0){
            let card = document.querySelectorAll("div.card");
            let productName = document.querySelectorAll("h5.product_name");
            let description = document.querySelectorAll("p.description");
            let imageUrl = document.querySelectorAll("img.image_url");
            let price = document.querySelectorAll("p.price");

            for (let i = 0; i < cardCount; i++) {
                //如果資料少於12筆，刪除多餘的card
                if (data.result.length <= i) {
                    card[i].style.display = "none";
                } else {
                    card[i].style.display = "flex";
                    productName[i].innerText = data.result[i].productName;
                    description[i].innerText = data.result[i].description;
                    price[i].children[1].innerText = data.result[i].price;
                    imageUrl[i].src = data.result[i].imageUrl;

                    productName[i].style.backgroundColor = "rgba(0, 0, 0, 0)"
                    description[i].style.backgroundColor = "rgba(0, 0, 0, 0)"
                    price[i].style.backgroundColor = "rgba(0, 0, 0, 0)"
                }
            }
        }

        if(currentData == totalData){
            console.log(false);
            return false;
        }
        else{
            console.log(true);
            return true;
        }
    })
}