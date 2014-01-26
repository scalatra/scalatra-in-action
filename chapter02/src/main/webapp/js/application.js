$(function() {
  "use strict";

  // The author name is our main piece of local state
  var author = null;

  // User interface elements
  var connectionIndicator = $('#connection-indicator');
  var chatInput = $('#chat-input');
  var peopleDisplay = $('#people');
  var loginButton = $('#loginButton');


  // Atmosphere socket-related variables
  var socket = $.atmosphere;
  var subSocket;
  var transport = 'websocket';

  /* Set up all the Atmosphere request callbacks. */

  var request = {
    url: "/the-chat",
    contentType: "application/json",
    transport: transport,
    trackMessageLength : true,
    fallbackTransport: 'long-polling'
  };

  // When the Atmosphere connection becomes available, 
  // show the connection indicator,
  // set the transport which the server will support, 
  // and focus the chatInput box.
  request.onOpen = function(response) {
    connectionIndicator.showConnected()
    transport = response.transport;
    chatInput.focus();
  };

  // When we receive a message, parse it as json 
  // and add it to the local peopleDisplay,
  // but only if we're not the message author.
  request.onMessage = function(rs) {
    var message = rs.responseBody;
    try {
      var json = jQuery.parseJSON(message);
    } catch (e) {
      console.log('Not a valid JSON object: ', message.data);
      return;
    }

    var me = json.author == author;
    if(!me) { peopleDisplay.say(json.author, json.message); }

  };

  // Show the disconnected icon when the connection closes.
  request.onClose = function(rs) {
    connectionIndicator.showDisconnected;
  };

  // Show the disconnected icon when we get a connection error, 
  // perhaps due to net failure.
  request.onError = function(rs) {
    connectionIndicator.showDisconnected;
  };

  // Subscribe to the Atmosphere socket. This is the crucial 
  // piece of the puzzle which actually
  // makes the connection between each browser and the live 
  // pipe to the server.
  subSocket = socket.subscribe(request);


  /* User interface code is all below this point */

  // Hide the chat input button initially.
  chatInput.hide();

  // Sends a message to the Atmosphere socket when the 
  // 'enter' key is pressed. Also adds the message to the 
  // local peopleDisplay.
  chatInput.keydown(function(e) {
    if (e.keyCode === 13) {
      var msg = $(this).val();
      $(this).val('');

      // Show the message locally.
      peopleDisplay.say(author, msg);

      // Pack up the message as JSON and send it to everybody else.
      var json = { author: author, message: msg };
      subSocket.push(jQuery.stringifyJSON(json));

    }
  });

  // Adds a chat popover to the peopleDisplay and 
  // fades it out after 5 seconds.
  peopleDisplay.say = function(author, message) {
    if(peopleDisplay.doesNotContain(author)) { 
      peopleDisplay.addPerson(author) 
    }
    var anchor = author == "server" ? $('#server') : $('#' + author)
    var placement = author == "server" ? 'bottom' : 'right'
    anchor.popover('destroy')
    anchor.popover({
      content: message, placement: placement
    }).popover('show')
    var pop = anchor.next()
    pop.delay(5000).fadeOut(500)
  }

  // Adds a person to the peopleDisplay box.
  peopleDisplay.addPerson = function(author){
    peopleDisplay.append(
      "<div class='row'><div class='span1' id='" + author + "'>" +
      "<img src='/img/person.png' class='img-polaroid'><p>"
      + author + "</p></div></div>")
  }


  // Check whether this author has already been added to the 
  // peopleDisplay. Returns true if the author should be added.
  peopleDisplay.doesNotContain = function(author) {
    return (author != "Big Brother" &&
      $("#" + author).length == 0) ? true : false
  }

  // Show the Atmosphere connected icon, a thumbs-up.
  connectionIndicator.showConnected = function(){
    this.addClass('icon-thumbs-up').
      removeClass('icon-thumbs-down')
  }

  // Show the Atmosphere disconnected icon, a thumbs-down.
  connectionIndicator.showDisconnected = function(){
    this.addClass('icon-thumbs-down').
      removeClass('icon-thumbs-up')
  }

  // Set up the user and show the chat interface.
  $('#loginForm').submit(function(event){
    author = $('#inputUsername').val();
    $('#loginModal').modal('hide');
    loginButton.hide();
    chatInput.show().focus();
    return false;
  });
});