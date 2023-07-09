let deleteProductForm = document.querySelector("form#deleteProduct");
deleteProductForm.addEventListener("submit", e => {
    //防止表單自動提交
    e.preventDefault();

    let productId = document.querySelector("#productId").value;

    let url = "http://localhost:8080/product/delete/" + productId;

    let requestInit = {
        method: "DELETE",
    }

    fetch(url, requestInit).then(response => {
        if(response.ok){
            console.log(response);
            return response.text();
        }
        else if(response.status == 404){
            let showResponse = document.querySelector("p.responseData");
            showResponse.innerHTML = "沒有第" + productId + "筆資料，請重新確認"
            throw new Error("請求失敗，狀態碼： " + response.status);
        }
    }).then(data => {
        console.log(data);

        let showResponse = document.querySelector("p.responseData");
        showResponse.innerHTML = data;
    })
})