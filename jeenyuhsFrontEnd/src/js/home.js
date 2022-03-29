let writePostForm = document.querySelector('form');

let user = localStorage.getItem("name");
let email = localStorage.getItem("email");

let socket = new WebSocket("ws://localhost:7070/group/home/"+email);

socket.addEventListener('message', function (event){
    let data = JSON.parse(event.data);
    createPost(data);
})

writePostForm.addEventListener('submit',(event) => {
    event.preventDefault();

    let data = formValues();


    let post = {
        "message": data.message,
        "likes": 0,
        "team": data.team,
        "person": email
    }

    socket.send(JSON.stringify(post));
})

function createPost({id,message,likes,team,person}){
    let div = document.createElement('div');
    let header = document.createElement('p');
    let text = document.createElement('p');

    let main = document.getElementById('posts');

    header.innerHTML = "@"+person.split("@")[0];
    text.innerHTML = message;

    div.appendChild(header);
    div.appendChild(text);

    div.classList.add("post");
    main.insertAdjacentElement("afterbegin",div);
}

function formValues(){
    let message = writePostForm.elements.namedItem("message").value;
    return {
        "message": message,
        "team": "home"
    }
}

async function loadConversions() {

    await fetch("http://localhost:7070/post/team/home")
        .then(getResponse)
        .then(oldConversion)
        .catch(err=>console.log(err));
}

function oldConversion(posts){
    posts.map((post)=>{
        createPost(post);
    })
}


function getResponse(response){
    return response.json();
}

function writeServer(action, data={}) {
    return { method: action, headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) }
}