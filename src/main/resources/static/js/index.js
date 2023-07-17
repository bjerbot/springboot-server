let nav=document.querySelector("nav");

window.addEventListener("scroll", ()=>{
    if(window.scrollY == 0){
        nav.style.boxShadow= "";
    }else{
        nav.style.boxShadow="0 10px 6px -6px #777";
    }
})

//
let divRightPart = document.querySelector("div.right-part");
for(let i = 0; i < 19; i++) {
    let cloneCard = document.querySelector("div.card").cloneNode(true);

    divRightPart.appendChild(cloneCard);
}

let url = "http://localhost:8080/product/query_count/20";

fetch(url).then(response => {
    if(response.ok){
        console.log(response);
        return response.json();
    }else if(response.status == 404){
        let showResponse = document.querySelector("p.responseData");
        showResponse.innerHTML = "沒有第 " + productId + "筆資料，請重新確認";
        throw new Error("請求失敗，狀態碼： " + response.status);
    }
}).then(data => {
    let productName = document.querySelectorAll("h5.product_name");
    let description = document.querySelectorAll("p.description");
    let imageUrl = document.querySelectorAll("img.image_url");
    let price = document.querySelectorAll("p.price");
    for(let i = 0; i < 20; i++){
        productName[i].innerText = data[i].productName;
        description[i].innerText = data[i].description;
        price[i].children[1].innerText = data[i].price;
        imageUrl[i].src = data[i].imageUrl;

        productName[i].style.backgroundColor = "rgba(0, 0, 0, 0)"
        description[i].style.backgroundColor = "rgba(0, 0, 0, 0)"
        price[i].style.backgroundColor = "rgba(0, 0, 0, 0)"
    }
})
