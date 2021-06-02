let singleUploadForm = document.querySelector('#singleUploadForm');
let singleFileUploadInput = document.querySelector('#singleFileUploadInput');
let singleFileUploadError = document.querySelector('#singleFileUploadError');
let singleFileUploadSuccess = document
    .querySelector('#singleFileUploadSuccess');
let uploadBool = false;

let fileDownloadUri;


function resetForm(){
    for (let i = 0; i < singleUploadForm.elements.length; i++) {
        let elemUser = singleUploadForm.elements[i];
        if(elemUser.type == "text"){
            elemUser.value = "";
        }
        if(elemUser.type == "file"){
            elemUser.value = "";
        }
    }
}


function uploadSingleFile(file) {

    let formData = new FormData();
    formData.append("file", file);


    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadFile");

    xhr.onload = function() {
        console.log(xhr.responseText);
        let response = JSON.parse(xhr.responseText);
        if (xhr.status == 200) {

            fileDownloadUri = response.fileDownloadUri;


            document.getElementsByClassName('submit_b')[0].innerHTML = "Success";

        } else {

            document.getElementsByClassName('submit_b')[0].innerHTML = "Try again";
        }
    }

    xhr.send(formData);
}


function readFormRegistration(){

    let firstName = document.querySelector('#firstName').value;
    let lastName = document.querySelector('#lastName').value;
    let age = document.querySelector('#age').value;

    if (age == '' || firstName == '' || lastName == "") {
        alert("Please fill all fields...!!!!!!");
        uploadBool = false;
    } else {

        uploadBool = true;

        let userData = {
            firstName: firstName,
            lastName: lastName,
            age: age

        };

        let xhr = new XMLHttpRequest();
        xhr.open("POST", "/registration");
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.send(JSON.stringify(userData));
    }
}

review.onclick = function(event) {
    resetForm();
    document.getElementsByClassName('submit_b')[0].innerHTML = "Submit";

    let fileId = "";
    let urlContent = fileDownloadUri.split('/');

    fileId = urlContent[urlContent.length-1];

    let formData = new FormData();
    formData.append("fileId", fileId);
    let xhr = new XMLHttpRequest();
    xhr.open("GET", fileDownloadUri);

    xhr.send(formData);
}


singleUploadForm.addEventListener('submit', function(event) {
    uploadBool = true;
    let files = singleFileUploadInput.files;
    if (files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }

    setTimeout(function() {
        readFormRegistration();
    }, 0);

    setTimeout(function() {
        (uploadBool && uploadSingleFile(files[0]));
        uploadBool = true;
    }, 0);

    event.preventDefault();
}, true);