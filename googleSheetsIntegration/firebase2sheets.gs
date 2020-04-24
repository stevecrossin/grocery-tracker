/* Function that allows the Firebase Data to be synchronised with Google Sheets. 

NOTE: You need to ensure you have the Firebase Library installed in Google Scripts. 
Refer here: https://sites.google.com/site/scriptsexamples/new-connectors-to-google-services/firebase

TODO #1: Currently quite Raw (as at 5th April). Connection established but need to extract the JSON Key value pairs properly to integrate
with csv-style structure in Google Sheets. 

TODO #2: The above should not be done until we correctly format the structure of purchasing data within firebase. 


*/ 

function writeSheets() {
  
  // Establish the Connection between the spreadsheet and the correct Firebase Repo.
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sheet = ss.getSheetByName('Sheet1');
  
  // Firebase credentials
  var firebaseUrl = "https://grocerytracker-d0dc2.firebaseio.com/Receipts/";
  var secret = "3M4P23opVjXTRExD0P6yvBsjAhDyhjGz0GhC9j4A";  
  
  
  // 
  var base = FirebaseApp.getDatabaseByUrl(firebaseUrl);
  var data = base.getData();
  var keys = Object.entries(data);
  var sheetRow = [];
  var entryKeys;
  for (index in keys) {
    sheetRow = [];
    entryKeys = Object.entries(data)
    for (i in entryKeys) {
      sheetRow.push(data);
    }
    //Logger.log(sheetRow);
    sheet.appendRow(sheetRow);                            
  }
}

