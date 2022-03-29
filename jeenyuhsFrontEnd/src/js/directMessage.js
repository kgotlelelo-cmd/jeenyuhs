let email = localStorage.getItem("email");
let user = localStorage.getItem("name");

let title = document.querySelector('title');
let chatter = localStorage.getItem("chatName");
title.innerHTML = chatter;

let form = document.querySelector('#message-post-form');



let socket = new WebSocket("ws://localhost:7070/dm/direct-message/"+email);

socket.addEventListener('message',(event)=>{
    let data = JSON.parse(event.data);
    createDm(data);
})

form.addEventListener('submit',(event)=>{
    event.preventDefault();
    let message = form.elements.namedItem('post-message');
    let post = {
        "message": message,
        "likes": 0,
        "team": chatter,
        "person": email
    }

    socket.send(JSON.stringify(post));
})

function getResponse(response){
    return response.json();
}

async function loadOnline(){
    await fetch("http://localhost:7070/post/team/home")
        .then(getResponse)
        .then(loadOnlineCards)
        .catch(err=>console.log(err))
}

function loadOnlineCards(names){
    names.map((name)=>{
        createOnlineCard(name);
    })
}

function createOnlineCard({email}){
    let div = document.createElement('div');
    let title = document.createElement('p');
    let text = document.createElement('p');

    title.innerHTML = email.split("@")[0] + " is online";
    title.innerHTML = "click here to chat";

    div.appendChild(title);
    div.appendChild(text);

    div.addEventListener('click',()=>{
        localStorage.setItem("chatName",email.split("@")[0]);
    });

    let mainDiv = document.getElementById('online-section');
    mainDiv.insertAdjacentElement("afterbegin",div);
}



function createDm({id,message,likes,team,person}){
    let div = document.createElement('div');
    let title = document.createElement('p');
    let text = document.createElement('p');

    if (person === email){
        title.innerHTML = "You said:";
    }else if (team === email){
        title.innerHTML = chatter+ " said:"
    }
    text.innerHTML = message;
    div.appendChild(title);
    div.appendChild(text);

    let section = document.getElementById('message-body');
    section.insertAdjacentElement("afterbegin",div)
}