// INTEGRATION OF USER TABLES WITH GOOGLE SHEETS. 


function userTableSync() {

// Establish the Connection between the spreadsheet and the correct Firebase Repo.
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sheet = ss.getSheetByName('User Table');
  
  // Firebase credentials
  var firebaseUrl = "https://grocerytracker-d0dc2.firebaseio.com/Users/";
  var secret = "3M4P23opVjXTRExD0P6yvBsjAhDyhjGz0GhC9j4A";  
    // 
  var base = FirebaseApp.getDatabaseByUrl(firebaseUrl);
  var data = base.getData();
  var keys = Object.entries(data);
  var sheetRow = [];
  var entryKeys;


  // cycle through the items in firebase and map them as json-style key-value pairs. 
  for (index in keys) {
          myobject = keys[index]
          userObject = Object.values(data).map(object => object.user)
          
          
          // initialise a new row to place in to the sheet
          var sheetRow = []
          
          // extract main values of interest from firebase. 
          email = Object.values(userObject).map(object => object.email)
          houseHoldAdults = Object.values(userObject).map(object => object.houseHoldAdults)
          houseHoldChildren = Object.values(userObject).map(object => object.houseHoldChildren)
          householdMembers = Object.values(userObject).map(object => object.householdMembers)
          loggedIn = Object.values(userObject).map(object => object.loggedIn)
          postCode = Object.values(userObject).map(object => object.postCode)
          shopNumber = Object.values(userObject).map(object => object.shopNumber)
          userAge = Object.values(userObject).map(object => object.userAge)
          userGender = Object.values(userObject).map(object => object.userGender)
          userHeight = Object.values(userObject).map(object => object.userHeight)
          userID = Object.values(userObject).map(object => object.userID)
          userName = Object.values(userObject).map(object => object.userName)
          userWeight = Object.values(userObject).map(object => object.userWeight)
          
          
          email1 = userObject[index].email
          houseHoldAdults1 = userObject[index].houseHoldAdults
          houseHoldChildren1 = userObject[index].houseHoldChildren
          householdMembers1 = userObject[index].householdMembers
          loggedIn1 = userObject[index].loggedIn
          postCode1 = userObject[index].postCode
          shopNumber1 = userObject[index].shopNumber
          userAge1 = userObject[index].userAge
          userGender1 = userObject[index].userGender
          userHeight1 = userObject[index].userHeight
          userID1 = userObject[index].userID
          userName1 = userObject[index].userName
          userWeight1 = userObject[index].userWeight
          
  
       
          // here, we check to see if each database has already been updated on google sheets. 
          // if it has not, we will extract the csv-stule information, and reconstruct it to suit our structure.
          syncCheck = userObject[index].synchronised


          if(syncCheck == "True") { }  // i.e. record already added to google sheets
          else {          
          
            // deconstruct the csv and reconstruct it for our needs. 
            var sheetRow = new Array();

            sheetRow.push(email1);
            sheetRow.push(userName1);
            sheetRow.push(userID1);
            sheetRow.push(userWeight1);
            sheetRow.push(userHeight1);
            sheetRow.push(userGender1);
            sheetRow.push(userAge1);
            sheetRow.push(houseHoldAdults1);
            sheetRow.push(houseHoldChildren1);
            sheetRow.push(householdMembers1);
            sheetRow.push(postCode1);
            sheetRow.push(shopNumber1);
//            sheetRow.push(loggedIn1);
      
            sheet.appendRow(sheetRow)
              
              // now that  we have done this, we need to make sure the newly added records are flagged as 
              // 'synchronised' so we dont add them again. 
              
              var object_name = Object.values(myobject)[0];
              var slash = '/';
              var receiptconcat = 'user';
              var syncCheckJson = slash.concat(object_name, slash, receiptconcat, slash, 'synchronised')
              base.setData(syncCheckJson, "True");
//              
            
          }

    }
  
}


