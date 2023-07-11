let createProductForm = document.querySelector("form#createProduct");
createProductForm.addEventListener("submit", e => {
    //防止表單自動提交
    e.preventDefault();

    let productName =  document.querySelector("#productName").value;
    let category = document.querySelector("#category").value;
    let imageUrl = document.querySelector("#imageUrl").value;
    let price = document.querySelector("#price").value;
    let stock = document.querySelector("#stock").value;
    let description = document.querySelector("#description").value;

    let createProduct = {
        productName,
        category,
        imageUrl,
        price,
        stock,
        description,
    }

    let requestInit = {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(createProduct)
    }

    let url = "http://localhost:8080/product/create";

    console.log(url);

    fetch(url, requestInit).then(response => {
        if(response.ok){
            console.log(response);
            return response.json();
        }
    }).then(data => {
        console.log(data);
        let dataToStr = JSON.stringify(data, null, "<br>");
        console.log(dataToStr);

        let showResponse = document.querySelector("p.responseData");
        showResponse.innerHTML = dataToStr;
    })
})