let searchProductForm = document.querySelector("form#getProductById");
searchProductForm.addEventListener("submit", e => {
    //防止表單自動提交
    e.preventDefault();

    let productId = document.querySelector("#productId").value;

    let action = "http://localhost:8080/product/" + productId;

    console.log(action);

    fetch(action).then(response => {
        if(response.ok){
            console.log(response);
            return response.json();
        }
    }).then(data => {
        console.log(data);
        console.log(typeof data);
        let dataToStr = JSON.stringify(data, null, "<br>");

        console.log(dataToStr);

        let showResponse = searchProductForm.children[3];
        console.log(showResponse);
        showResponse.innerHTML = dataToStr;
    })
})