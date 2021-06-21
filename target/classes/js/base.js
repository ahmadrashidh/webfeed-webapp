var posts = [];

loadPosts();

// Load Posts Begin

function loadPosts() {

    console.log("Loading Posts..");

    var xhr = new XMLHttpRequest();

    xhr.open('GET', "http://localhost:8080/posts/", true);

    xhr.onload = function () {

        if (this.status == 200) {
            posts = JSON.parse(this.response);
            renderPosts();
        }
    }

    xhr.send();

}


function renderPosts() {

    console.log("Rendering Posts..");

    posts.map((post, index) => {

        var timeAgo = getTimeAgo(new Date(post.createdDate));

        var postTemplate = document.getElementById('post-section');
        var postDom = postTemplate.content.cloneNode(true);

        postDom.getElementById('post-section-body').id = "post-section-body-" + post.id;
        postDom.getElementById('post-body').id = "post-body-" + post.id;
        postDom.getElementById('post-text').innerText = post.text;
        postDom.getElementById('post-time').innerText = timeAgo;
        postDom.getElementById('like-btn').id = "like-btn-" + post.id;
        postDom.getElementById('comment-show-btn').id = "comment-show-btn-" + post.id;
        document.getElementById('feed').appendChild(postDom);

        addPostEvent(post.id);

    })


}

function getTimeAgo(createdTime) {

    var elapsedTime = (Date.now() - createdTime) / 1000;
    var suffix = " seconds ago";
    if (elapsedTime > 60) {

        elapsedTime = elapsedTime / 60;
        suffix = elapsedTime > 1 ? " minutes ago" : "minute ago";
        if (elapsedTime > 60) {
            elapsedTime = elapsedTime / 60;
            suffix = elapsedTime > 1 ? " hours ago" : "hour ago";
            if (elapsedTime > 24) {
                elapsedTime = elapsedTime / 24;
                suffix = elapsedTime > 1 ? " days ago" : "day ago";
            }
        }
    }

    console.log("ElapsedTime:" + (elapsedTime + suffix));
    return Math.round(elapsedTime) + suffix;

}



function addPostEvent(postId) {

    console.log("Adding event listener to post");

    var likeBtnId = "like-btn-" + postId;
    var commentBtnId = "comment-show-btn-" + postId;

    document.getElementById(likeBtnId).addEventListener('click', function () {
        console.log("Liking Post");
        likePost(postId);
    });

    document.getElementById(commentBtnId).addEventListener('click', function () {
        console.log("Showing Comment Box");
        renderCommentInput(postId, postId, false);
        loadComments(postId);
    });

}

// Load Posts End

// Sharing Post - Beginning

function sharePost() {

    console.log("Clicked Share post");

    var text = document.getElementById('post-input').value.trim();
    isInvalid = validatePostText(text);
    if (isInvalid) {

        return;
    }

    var post = {
        postText: text
    }

    sendPost(post);


}

function sendPost(post) {

    console.log("Sending Post");
    var xhr = new XMLHttpRequest();

    xhr.open('POST', "http://localhost:8080/posts", true);

    xhr.onload = function () {
        if (this.status == 201)
            loadPosts();
    }

    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.setRequestHeader("Accept", "application/json");
    xhr.send(JSON.stringify(post));

}

function validatePostText(text) {

    console.log("Validating Post");
    if (text == "") {
        document.getElementById('invalid-text').innerHTML = "Say what do you want to share?";
        return true;
    } else {
        document.getElementById('invalid-text').innerHTML = "";
        return false;
    }

}

// Sharing Post End





// Like Post Beginning

function likePost(postId) {

    var json = {
        likedBy: "Ahmad"
    }

    var xhr = new XMLHttpRequest();

    xhr.open('POST', `http://localhost:8080/posts/${postId}/likes`, true);

    xhr.onload = function () {
        if (this.status == 200)
            renderLike(postId);
    }

    xhr.send(JSON.stringify(json));

}

function renderLike(postId) {
    var likeBtnId = "like-btn-" + postId;
    document.getElementById(likeBtnId).classList.add('liked');
}

// Like End

// Comment Beginning

function renderCommentInput(id, parentId, isComment) {

    console.log("Rendering Comment Input for", id);
    
    var commentInputTemplate = document.getElementById('comment-section');
    if (commentInputTemplate.content == undefined) {
        commentInputTemplate = document.getElementById('comment-section-alternate');
    }
    console.log(commentInputTemplate.content);
    var commentInputDom = commentInputTemplate.content.cloneNode(true);

    commentInputDom.getElementById('comment-section-body').id = "comment-section-body-" + id;
    commentInputDom.getElementById('comment-block').id = "comment-block-" + id;
    commentInputDom.getElementById('comment-body').id = "comment-body-" + id;
    commentInputDom.getElementById('comment-input').id = "comment-input-" + id;
    commentInputDom.getElementById('comment-btn').id = "comment-btn-" + id;


    if(isComment){
        document.getElementById(`comment-content-body-${parentId}`).appendChild(commentInputDom);
    } else {
        document.getElementById(`post-body-${parentId}`).appendChild(commentInputDom);
    }


    
}


function loadComments(postId) {

    console.log("Loading Comments..");

    var xhr = new XMLHttpRequest();

    xhr.open('GET', `http://localhost:8080/posts/${postId}/comments`, true);

    xhr.onload = function () {
        if (this.status == 200) {
            console.log("Comments Recieved", this.responseText);
            renderComments(JSON.parse(this.responseText), postId);
        }

    }

    xhr.send();

}

function renderComments(comments, postId) {

    console.log("Rendering Comments", comments);

    comments.map((comment, index) => {

        console.log(comment);
        var commentTemplate = document.getElementById('comment-content');
        var commentDom = commentTemplate.content.cloneNode(true);

        commentDom.getElementById('comment-text').innerText = comment.text;
        commentDom.getElementById('comment-content-block').id = "comment-content-block-" + comment.id;
        commentDom.getElementById('comment-content-body').id = "comment-content-body-" + comment.id;
        commentDom.getElementById('comment-show-btn').innerText = "Comment (" + comment.comments.length + ")";
        commentDom.getElementById('comment-show-btn').id = "comment-show-btn-" + comment.id;

        document.getElementById(`comment-section-body-${postId}`).appendChild(commentDom);

        addCommentEvent(comment.comments, comment.id);

    })
}

function addCommentEvent(comment, id){
    var commentBtnId = 'comment-show-btn-' + id;
    document.getElementById(commentBtnId).addEventListener('click', function () {
        console.log("Showing Comment Box");
        renderSubcomments(comment, id);
    });

}
function renderSubcomments(comment, id){

    renderCommentInput(id, id, true);

    renderComments(comment, id);
}




document.getElementById('share-btn').addEventListener('click', sharePost);