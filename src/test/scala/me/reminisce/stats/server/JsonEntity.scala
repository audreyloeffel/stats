package me.reminisce.stats.server

object JsonEntity {
  val game: (String, String, String) => String = (id: String, uid1: String, uid2: String) => s"""{
  "_id": "$id",
  "player1": "$uid1",
  "player2": "$uid2",
  "player1Board": {
    "_id": "6ec1a0a70a3685bf49a92fe7",
    "userId": "10204239988860988",
    "tiles": [
      {
        "_id": "73ac958345bdc3f42553d9da",
        "type": "Misc",
        "question1": {
          "type": "GeoWhatCoordinatesWereYouAt",
          "kind": "Geolocation",
          "correct": false,
          "timeSpent" : 4000
        },
        "question2": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline",
          "correct": false,
          "timeSpent": 4000
        },
        "question3": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline",
          "correct": false,
          "timeSpent": 4000
        },
        "score": 0,
        "answered": true,
        "disabled": true
      },
      {
        "_id": "0dba51e83cea5f2e721288c6",
        "type": "Order",
        "question1": {
          "type": "ORDPostTime",
          "kind": "Order"
        },
        "question2": {
          "type": "ORDPostLikesNumber",
          "kind": "Order"
        },
        "question3": {
          "type": "ORDPostLikesNumber",
          "kind": "Order"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      },
      {
        "_id": "daf78788853dd1d860fdffcd",
        "type": "Timeline",
        "question1": {
          "type": "TLWhenDidYouLikeThisPage",
          "kind": "Timeline",
          "correct": true,
          "timeSpent": 4000
        },
        "question2": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline",
          "correct": false,
          "timeSpent": 4000
        },
        "question3": {
          "type": "TLWhenDidYouLikeThisPage",
          "kind": "Timeline",
          "correct": false,
          "timeSpent": 4000
        },
        "score": 1,
        "answered": true,
        "disabled": true
      },
      {
        "_id": "0b96a6c7221c9c75c0c1d5f8",
        "type": "MultipleChoice",
        "question1": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice"
        },
        "question2": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice"
        },
        "question3": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      },
      {
        "_id": "91017502fea813bdf3d36794",
        "type": "MultipleChoice",
        "question1": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice",
          "correct": true,
          "timeSpent": 4000
        },
        "question2": {
          "type": "MCWhoMadeThisCommentOnYourPost",
          "kind": "MultipleChoice",
          "correct": true,
          "timeSpent": 4000
        },
        "question3": {
          "type": "MCWhoMadeThisCommentOnYourPost",
          "kind": "MultipleChoice",
          "correct": false,
          "timeSpent": 4000
        },
        "score": 2,
        "answered": true,
        "disabled": true
      },
      {
        "_id": "628dbf9168e9054db125d427",
        "type": "Misc",
        "question1": {
          "type": "MCWhoMadeThisCommentOnYourPost",
          "kind": "MultipleChoice"
        },
        "question2": {
          "type": "ORDPostLikesNumber",
          "kind": "Order"
        },
        "question3": {
          "type": "ORDPostLikesNumber",
          "kind": "Order"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      },
      {
        "_id": "19d9c45e0261a90deb2ce7c2",
        "type": "Geolocation",
        "question1": {
          "type": "GeoWhatCoordinatesWereYouAt",
          "kind": "Geolocation",
          "correct": false,
          "timeSpent": 4000
        },
        "question2": {
          "type": "GeoWhatCoordinatesWereYouAt",
          "kind": "Geolocation",
          "correct": true,
          "timeSpent": 4000
        },
        "question3": {
          "type": "GeoWhatCoordinatesWereYouAt",
          "kind": "Geolocation",
          "correct": false,
          "timeSpent": 4000
        },
        "score": 1,
        "answered": true,
        "disabled": true
      },
      {
        "_id": "c3476a91098b2ec07f95b9d0",
        "type": "Timeline",
        "question1": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline"
        },
        "question2": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline"
        },
        "question3": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      },
      {
        "_id": "658f29649eb6fe2d833cb003",
        "type": "Order",
        "question1": {
          "type": "ORDPostTime",
          "kind": "Order"
        },
        "question2": {
          "type": "ORDPostTime",
          "kind": "Order"
        },
        "question3": {
          "type": "ORDPostTime",
          "kind": "Order"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      }
    ]
  },
  "player2Board": {
    "_id": "2bba572c1e5ea90e592c3377",
    "userId": "aeXzFKM4uMDGqimoB",
    "tiles": [
      {
        "_id": "215ef3d8568c007a2c158c53",
        "type": "Order",
        "question1": {
          "type": "ORDPageLikeTime",
          "kind": "Order",
          "correct": true,
          "timeSpent": 4000
        },
        "question2": {
          "type": "ORDPageLikeTime",
          "kind": "Order",
          "correct": true,
          "timeSpent": 4000
        },
        "question3": {
          "type": "ORDPageLikes",
          "kind": "Order",
          "correct": true,
          "timeSpent": 4000
        },
        "score": 3,
        "answered": true,
        "disabled": true
      },
      {
        "_id": "47e3c4de33dc32ae3aadf37e",
        "type": "Timeline",
        "question1": {
          "type": "TLWhenDidYouLikeThisPage",
          "kind": "Timeline"
        },
        "question2": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline"
        },
        "question3": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      },
      {
        "_id": "31c9e2cef8cb79d0c9bab1f7",
        "type": "MultipleChoice",
        "question1": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice",
          "correct": false,
          "timeSpent": 4000
        },
        "question2": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice",
          "correct": false,
          "timeSpent": 4000
        },
        "question3": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice",
          "correct": false,
          "timeSpent": 4000
        },
        "score": 0,
        "answered": true,
        "disabled": true
      },
      {
        "_id": "b4b118b40bb2ac8f85e6dfc0",
        "type": "Order",
        "question1": {
          "type": "ORDPageLikeTime",
          "kind": "Order"
        },
        "question2": {
          "type": "ORDPageLikeTime",
          "kind": "Order"
        },
        "question3": {
          "type": "ORDPostTime",
          "kind": "Order"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      },
      {
        "_id": "d200419962c66a319af4aab3",
        "type": "Order",
        "question1": {
          "type": "ORDPostLikesNumber",
          "kind": "Order"
        },
        "question2": {
          "type": "ORDPageLikes",
          "kind": "Order"
        },
        "question3": {
          "type": "ORDPageLikes",
          "kind": "Order"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      },
      {
        "_id": "c8f944d7de67346493704093",
        "type": "Timeline",
        "question1": {
          "type": "TLWhenDidYouLikeThisPage",
          "kind": "Timeline"
        },
        "question2": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline"
        },
        "question3": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      },
      {
        "_id": "a8e688eeb4d559e94a20e5e3",
        "type": "Timeline",
        "question1": {
          "type": "TLWhenDidYouLikeThisPage",
          "kind": "Timeline"
        },
        "question2": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline"
        },
        "question3": {
          "type": "TLWhenDidYouShareThisPost",
          "kind": "Timeline"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      },
      {
        "_id": "47cbe5abc42b7ed6a5942460",
        "type": "MultipleChoice",
        "question1": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice"
        },
        "question2": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice"
        },
        "question3": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice"
        },
        "score": 0,
        "answered": false,
        "disabled": false
      },
      {
        "_id": "014f9fd3266412b0e6291254",
        "type": "MultipleChoice",
        "question1": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice",
          "correct": false,
          "timeSpent": 4000
        },
        "question2": {
          "type": "MCWhoLikedYourPost",
          "kind": "MultipleChoice",
          "correct": false,
          "timeSpent": 4000
        },
        "question3": {
          "type": "MCWhoMadeThisCommentOnYourPost",
          "kind": "MultipleChoice",
          "correct": false,
          "timeSpent": 4000
        },
        "score": 0,
        "answered": true,
        "disabled": true
      }
    ]
  },
  "status": "ended",
  "player1Score": 4,
  "player2Score": 3,
  "wonBy": 1,
  "creationTime": 1477500502075
}

"""
}