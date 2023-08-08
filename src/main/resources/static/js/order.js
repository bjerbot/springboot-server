//取得sessionStorage資訊
let user = JSON.parse(sessionStorage.getItem("user"));
let token = sessionStorage.getItem("token");
console.log(user);
console.log(token);

if(user == null || token == null){
    window.location.href = "../index.html";
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

    userMenu.style.display = "none";
    userEmailBtn.style.display = "none";
    window.location.href = "../index.html";
});

//驗證使用者
if(user != null && token != null){
    email = user.email;

    let validateUrl = "http://localhost:8080/validate_login_status";

    let body = {
        email
    }

    fetch(validateUrl, {
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
                // window.location.href = "../index.html";
            }
        });
}

//取得訂單紀錄
let getOrderUrl = "http://localhost:8080/users/" + user.userId + "/order";

fetch(getOrderUrl, {
    method:"GET",
})
    .then(response => {
        return response.json();
    })
    .then(data => {
        console.log(data);

        //將data中的result(order)設置到新槍建的row
        let orderCount = data.result.length;
        for(let i = 0; i < orderCount; i++){
            let order = document.createElement("tr");
            order.id = "order"

            let orderId = document.createElement("td");
            let createdDate = document.createElement("td");
            let totalAmount = document.createElement("td");

            orderId.id = "order_id";
            createdDate.id = "created_date";
            totalAmount.id = "total_amount";

            orderId.textContent = data.result[i]["orderId"];
            createdDate.textContent = data.result[i]["createdDate"];
            totalAmount.textContent = data.result[i]["total_amount"];

            order.appendChild(orderId);
            order.appendChild(createdDate);
            order.appendChild(totalAmount);

            let tbody = document.querySelector("table#order_table tbody");
            tbody.appendChild(order);
        }

        //將尚未有訂單紀錄的文字隱藏
        if(data.result.length > 0){
            let p = document.querySelector("p#not_order_history");
            p.style.display = "none";
        }

        //取得訂單紀錄的細項

        let orderList = document.querySelectorAll("table#order_table tbody tr");

        //設置order的點擊事件
        orderList.forEach((order, orderIndex) => {
            order.addEventListener("click", e => {

                let newTableContainer = document.createElement("div");
                newTableContainer.style.display = "inline-block";
                newTableContainer.style.position = "relative";
                newTableContainer.style.left = "50%";

                let newTable = document.createElement("table");
                newTable.style.width = "600px";

                let newHeader = newTable.createTHead();
                let newHeaderData = `
                        <tr>
                          <th class="img">圖片</th>
                          <th class="name">名稱</th>
                          <th class="quantity">數量</th>
                          <th class="quantity">單價</th>
                        </tr>`;

                newHeader.innerHTML = newHeaderData;

                //遍歷order當中的orderItem
                let orderItems = data.result[orderIndex]["orderItemList"];
                orderItems.forEach((item, orderItemIndex) => {
                    console.log(item["imageUrl"]);
                    console.log(item["productName"]);
                    console.log(item["quantity"]);
                    console.log(item["amount"]);

                    let newOrderItem = document.createElement("tr");

                    let newImgUrl = document.createElement("td");
                    newImgUrl.class = "img_url";
                    let newImg = document.createElement("img");
                    newImg.style.width = "50px";
                    newImg.style.height = "50px";
                    newImg.src = item["imageUrl"];
                    newImgUrl.appendChild(newImg);

                    let newProductName = document.createElement("td");
                    newProductName.class = "product_name";
                    newProductName.innerHTML = item["productName"];
                    let newQuantity = document.createElement("td");
                    newQuantity.class = "quantity";
                    newQuantity.innerHTML = item["quantity"];
                    let newAmount = document.createElement("td");
                    newAmount.class = "amount";
                    newAmount.innerHTML = item["amount"];

                    newOrderItem.appendChild(newImgUrl);
                    newOrderItem.appendChild(newProductName);
                    newOrderItem.appendChild(newQuantity);
                    newOrderItem.appendChild(newAmount);

                    newTable.appendChild(newOrderItem);
                    newTableContainer.appendChild(newTable);

                    let tbody = document.querySelector("table#order_table tbody");
                    console.log(tbody);
                    console.log(tbody.children[orderItemIndex]);
                    console.log(tbody.children[orderItemIndex].nextSibling);

                    if(orderIndex < orderCount-1){
                        tbody.insertBefore(newTableContainer, tbody.children[orderItemIndex].nextSibling);
                    }
                    else if(orderIndex == orderCount-1){
                        tbody.appendChild(newTableContainer)
                    }
                })
            })
        })
})




