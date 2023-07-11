let searchProductForm = document.querySelector("form#getProductById");

searchProductForm.addEventListener("submit", e => {
    //防止表單自動提交
    e.preventDefault();

    let productId = document.querySelector("#productId").value;

    let url;
    if(productId == ""){
        url = "http://localhost:8080/product/queryAll";
    }else{
        url = "http://localhost:8080/product/query/" + productId;
    }

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
        console.log(data);
        let dataToStr = JSON.stringify(data, null, "<br>");

        console.log(dataToStr);

        let showResponse = document.querySelector("p.responseData");
        showResponse.innerHTML = dataToStr;
    })
})