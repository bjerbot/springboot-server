//shadow animation
let nav=document.querySelector("nav");
window.addEventListener("scroll", ()=>{
    if(window.scrollY == 0){
        nav.style.boxShadow= "";
    }else{
        nav.style.boxShadow="0 10px 6px -6px #777";
    }
})

let currentPage = 1;
let cardCount = 12;
let offset = (currentPage-1)*cardCount;
let category = "all";

//clone商品列表
let cardContainer = document.querySelector("div.card_container");
for(let i = 0; i < cardCount-1; i++) {
    let cloneCard = document.querySelector("div.card").cloneNode(true);

    cardContainer.appendChild(cloneCard);
}

//請求商品列表資料
let categoryAllUrl = "http://localhost:8080/products?limit=" + cardCount + "&offset=" + offset;
console.log(categoryAllUrl);
queryProducts(categoryAllUrl);

//請求商品列表(按category FOOD)
let categoryFoodBtn = document.querySelector("li#categoryFood");

categoryFoodBtn.addEventListener("click", e => {
    e.preventDefault();

    currentPage = 1;
    cardCount = 12;
    offset = (currentPage-1)*cardCount;
    category = "FOOD"
    console.log(currentPage);
    console.log(offset);
    let categoryFoodUrl = "http://localhost:8080/products?limit=" + cardCount + "&offset=" + offset + "&category=" + category;

    queryProducts(categoryFoodUrl);
})

//請求商品列表(按category CAR)
let categoryCarBtn = document.querySelector("li#categoryCar");

categoryCarBtn.addEventListener("click", e => {
    e.preventDefault();

    currentPage = 1;
    cardCount = 12;
    offset = (currentPage-1)*cardCount;
    category = "CAR"
    console.log(currentPage);
    console.log(offset);
    let categoryCarUrl = "http://localhost:8080/products?limit=" + cardCount + "&offset=" + offset + "&category=" + category;

    queryProducts(categoryCarUrl);
})

//請求商品列表(按分頁)
let Btn_pre = document.querySelector("button#page_previous");
let Btn_next = document.querySelector("button#page_next");

Btn_next.addEventListener("click", e => {
    e.preventDefault();
    console.log("Btn_next");
    currentPage+=1;
    offset += cardCount;
    console.log(currentPage);
    console.log(offset);
    let pageUrl = "http://localhost:8080/products?limit=13&offset=" + offset;
    if(category != "all"){
        pageUrl=pageUrl + "&category=" + category;
    }

    queryProducts(pageUrl);
})

Btn_pre.addEventListener("click", e => {
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

        queryProducts(pageUrl);
    }
})

function queryProducts(url) {
    fetch(url).then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status == 404) {
            console.log("404")
        }
    }).then(data => {
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
    })
}