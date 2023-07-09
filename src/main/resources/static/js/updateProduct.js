let updateProductForm = document.querySelector("form#updateProduct");
updateProductForm.addEventListener("submit", e => {
    //防止表單自動提交
    e.preventDefault();

    let productId = document.querySelector("#productId").value;
    let productName =  document.querySelector("#productName").value;
    let category = document.querySelector("#category").value;
    let imageUrl = document.querySelector("#imageUrl").value;
    let price = document.querySelector("#price").value;
    let stock = document.querySelector("#stock").value;
    let description = document.querySelector("#description").value;

    let updateProduct = {
        productName,
        category,
        imageUrl,
        price,
        stock,
        description,
    }

    let requestInit = {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(updateProduct)
    }

    let url = "http://localhost:8080/product/update/"+productId;

    fetch(url, requestInit).then(response => {
        if(response.ok){
            return response.json();
        }
        else if(response.status == 404){
            let showResponse = document.querySelector("p.responseData");
            showResponse.innerHTML = "沒有第" + productId + "筆資料，請重新確認";
            throw new Error("請求失敗，狀態碼： " + response.status);
        }
    }).then(data => {
        console.log(data);
        let dataToStr = JSON.stringify(data, null, "<br>");
        console.log(dataToStr);

        let showResponse = document.querySelector("p.responseData");
        showResponse.innerHTML = dataToStr;
    })
})