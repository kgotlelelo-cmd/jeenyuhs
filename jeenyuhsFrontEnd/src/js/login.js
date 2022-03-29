let formElement = document.querySelector('form');

let url = "http://localhost:7070/login";

formElement.addEventListener("submit",async function (event) {
    event.preventDefault();
    let email = formElement.elements.namedItem("email").value;

    localStorage.setItem("name",email.split("@")[0]);
    localStorage.setItem("email",email);

    let data = {"email": email};

    await fetch(url,writeServer("POST", data))
        .then(getResponse)
        .catch(err=>console.log(err));

    window.location.href = "home.html";

})

function writeServer(action, data={}) {
    return { method: action, headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) }
}

function getResponse(response){
    return response.json();
}