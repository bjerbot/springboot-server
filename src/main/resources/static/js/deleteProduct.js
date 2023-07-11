let deleteProductForm = document.querySelector("form#deleteProduct");
deleteProductForm.addEventListener("submit", e => {
    //防止表單自動提交
    e.preventDefault();

    let productId = document.querySelector("#productId").value;

    let url = "http://localhost:8080/product/delete/" + productId;

    let requestInit = {
        method: "DELETE",
    }

    let showResponse = document.querySelector("p.responseData");

    fetch(url, requestInit).then(response => {
        if(response.status == 204){
            console.log(response);
            showResponse.innerHTML = "刪除成功"
        }
        else if(response.status == 404){
            showResponse.innerHTML = "請求失敗 bad request";
            throw new Error("請求失敗，狀態碼： " + response.status);
        }
    })
})