let user = localStorage.getItem("name");
let email = localStorage.getItem("email");


let header = document.querySelector('h1');
let title = document.querySelector('title');
title.innerHTML = user;
header.innerHTML = user;

async function loadConversions() {

    await fetch("http://localhost:7070/post/" + email)
        .then(getResponse)
        .then(oldConversion)
        .catch(err=>console.log(err));
}

function oldConversion(posts){
    posts.map((post)=>{
        createPost(post);
    })
}

function createPost({id,message,likes,team,person}){

    let div = document.createElement('div');
    let header = document.createElement('p');
    let text = document.createElement('p');

    let main = document.getElementById('profile-post');

    // header.innerHTML = "@"+person.split("@")[0];
    header.innerHTML = team.includes("@") ? "You said this in/to: "+ team.split("@")[0] :"You said this in/to: "+ team;

    text.innerHTML = message;

    div.appendChild(header);
    div.appendChild(text);

    main.insertAdjacentElement("afterbegin",div);
}

function getResponse(response){
    return response.json();
}