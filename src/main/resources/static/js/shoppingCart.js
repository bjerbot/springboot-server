// let newCartData =
//     `[
//         {
//             \"productName\":\"蘋果（澳洲)1\"\,
//             \"imgUrl\":\"https://cdn.pixabay.com/photo/2016/11/30/15/00/apples-1872997_1280.jpg\"\,
//             \"price\":\"30\"\,
//             \"description\":\"這是來自澳洲的蘋果！\"
//         },
//         {
//             \"productName\":\"蘋果（澳洲)2\"\,
//             \"imgUrl\":\"https://cdn.pixabay.com/photo/2016/11/30/15/00/apples-1872997_1280.jpg\"\,
//             \"price\":\"30\"\,
//             \"description\":\"這是來自澳洲的蘋果！\"
//         }
//     ]`;
//
// localStorage.setItem("cartData", newCartData);

//取得LocalStorage的購物車資訊並parse成JSON String
let cartDataStr = localStorage.getItem("cartData");

let cartData = JSON.parse(cartDataStr);
console.log(cartData);

//複製cartItem ，並依據購物車資訊cartData的長度 append進tbody，完成表格複製
if(cartData.length >= 1) {
    let emptyCartDiv = document.querySelector("div#emptyCartDiv");
    emptyCartDiv.style.display = "none";

    for (let i = 0; i < cartData.length; i++) {
        let cloneCartItem = document.createElement("tr");
        cloneCartItem.className = "cartItem";
        cloneCartItem.innerHTML =
            `
<!--            <td class="cartItem_ProductCheck"><input type="checkbox" class="checkbox"></td>-->
            <td class="cartItem_productImg"><img></td>
            <td class="cartItem_productName"><p>名稱</p></td>
            <td class="cartItem_quantity">
                <input class="minusBtn" type="button" value="-">
                <p>數量</p>
                <input class="plusBtn" type="button" value="+">
            </td>
            <td class="cartItem_amount"><span>價格：</span><span class="dollar_sign">$</span><span class="cartItem_amount_value">價格</span></td>
        `;
        let tbodyCart = document.querySelector("li#cartArea table tbody");
        tbodyCart.appendChild(cloneCartItem);
    }
}

//取得所有的cartItem，並串接顯示cartData的資訊
let cartItem = document.querySelectorAll("tr.cartItem");
console.log(cartItem);

//之後用來計算結帳總金額的array
let amountList = [];

cartItem.forEach((item, index) => {

    let cartItemImg = cartItem[index].children[0].children[0];
    let cartItemProductName = cartItem[index].children[1];
    let cartItemQuantity = cartItem[index].children[2].children[1];
    let cartItemAmount = cartItem[index].children[3];

    cartItemImg.src = cartData[index]["imgUrl"];
    cartItemProductName.innerText = cartData[index]["productName"];
    cartItemQuantity.innerHTML =  cartData[index]["quantity"];

    let quantity = Number(cartItemQuantity.innerHTML);
    let price = Number(cartData[index]["price"]);
    let amount = quantity * price;

    cartItemAmount.innerText = amount;

    amountList[index] = amount;
})

//增加商品數量
let plusBtn = document.querySelectorAll("input.plusBtn");
let plusBtnArray =Array.from(plusBtn);

plusBtnArray.forEach(btn => {
    btn.addEventListener("click", e => {
        let index = plusBtnArray.indexOf(e.target);

        //取得欄位
        let cartItemQuantity = cartItem[index].children[2].children[1];
        let cartItemAmount = cartItem[index].children[3];

        //取得cartData中的數量並`加一`
        let currentQuantity = Number(cartData[index]["quantity"]);
        currentQuantity++;

        //增加cartData的數量，並執行setItem儲存
        cartData[index]["quantity"] = currentQuantity;
        let cartDataStr = JSON.stringify(cartData);
        localStorage.setItem("cartData", cartDataStr);

        let price = Number(cartData[index]["price"]);
        let amount = currentQuantity * price;

        cartItemQuantity.innerHTML = "" + currentQuantity;
        cartItemAmount.innerText = amount;

        amountList[index] = amount;

        countTotalAmount();
    })
})

//減少商品數量
let minusBtn = document.querySelectorAll("input.minusBtn");
let minusBtnArray = Array.from(minusBtn);

minusBtnArray.forEach(btn => {
    btn.addEventListener("click", e => {
        console.log(minusBtnArray.indexOf(e.target));
        let index = minusBtnArray.indexOf(e.target);

        //取得欄位
        let cartItemQuantity = cartItem[index].children[2].children[1];
        let cartItemAmount = cartItem[index].children[3];

        //取得cartData中的數量並`減一`
        let currentQuantity = Number(cartItemQuantity.innerHTML);
        currentQuantity--;

        if (currentQuantity > 0) {
            //增加cartData的數量，並執行setItem儲存
            cartData[index]["quantity"] = currentQuantity;
            let cartDataStr = JSON.stringify(cartData);
            localStorage.setItem("cartData", cartDataStr);

            let price = Number(cartData[index]["price"]);
            let amount = currentQuantity * price;

            cartItemQuantity.innerHTML = "" + currentQuantity;
            cartItemAmount.innerText = amount;

            amountList[index] = amount;

            countTotalAmount();
        }
        else if(currentQuantity == 0){
            //刪除cartData中的商品，並執行setItem儲存

            cartData.splice(index, 1);
            console.log(cartData);

            let cartDataStr = JSON.stringify(cartData);
            localStorage.setItem("cartData", cartDataStr);

            //刪除購物車欄位
            cartItem[index].remove();
            console.log(cartItem);

            //依據index刪除Total_amount的數值
            amountList.splice(index, 1);
            console.log(amountList);

            countTotalAmount();

            //商品刪除後，重新更新購物車，使下一次刪除時讓cartData跟cartItem的index能夠正確對應
            cartItem = document.querySelectorAll("tr.cartItem");

            minusBtn = document.querySelectorAll("input.minusBtn");
            minusBtnArray = Array.from(minusBtn);

            plusBtn = document.querySelectorAll("input.plusBtn");
            plusBtnArray =Array.from(plusBtn);
        }

        if(cartData.length == 0){
            let emptyCartDiv = document.querySelector("div#emptyCartDiv");
            emptyCartDiv.style.display = "block";
        }
    })
})

//計算結帳總金額
let totalAmountResult = 0;

countTotalAmount();

function countTotalAmount(){
    totalAmountResult = 0;

    for(let i = 0; i < amountList.length; i++){

        totalAmountResult += amountList[i];
    }

    let totalAmountElement = document.querySelector("span#totalAmount_Elm");

    totalAmountElement.innerText = totalAmountResult;
}

//結帳送出訂單
let createOrderBtn = document.querySelector("input#createOrderBtn");

createOrderBtn.addEventListener("click", e => {
    console.log("createOrderBtn")
});
