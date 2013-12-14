package com.example.comments.data

import com.example.comments.models._

object CommentData {
  /**
   * Some fake comments data so we can simulate retrievals.
   */
  var all = List(
    Comment(
      "http://menus.intranet.local/meals/spaghetti", 
      "I like Spaghetti", 
      "The Spaghetti is usually pretty good, especially on Tuesdays"
    ),
    Comment(
      "http://customers.intranet.local/people/mr-smith", 
      "Likes a drink",
      "Particularly enjoys 3-martini lunches. Vodka martinis are tops with him."),
    Comment(
      "http://news.intranet.local/articles/share-price-up-22-per-cent", 
      "Great news", 
      "That's great to hear!"),
    Comment(
      "http://news.intranet.local/articles/share-price-up-22-per-cent", 
      "Like a boss", 
      "Promote synergy, direct workflow..."),       
    Comment(
      "http://news.intranet.local/articles/fitter-happier", 
      "More productive", 
      "Getting regular exercise at the gym")    
  )  
}

