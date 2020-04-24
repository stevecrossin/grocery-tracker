/*
Function that allows the Firebase Data to be synchronised with Google Sheets.
RG

*/

function writeSheets() {

  // Establish the Connection between the spreadsheet and the correct Firebase Repo.
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sheet = ss.getSheetByName('Receipt Table');

  // Firebase credentials
  var firebaseUrl = "https://grocerytracker-d0dc2.firebaseio.com/Receipts/";
  var secret = "3M4P23opVjXTRExD0P6yvBsjAhDyhjGz0GhC9j4A";
    //
  var base = FirebaseApp.getDatabaseByUrl(firebaseUrl);
  var data = base.getData();
  var keys = Object.entries(data);
  var sheetRow = [];
  var entryKeys;

// below not required. Set the spreadsheet up with this already.
//  sheet.appendRow(["Item Description", "Unit Price", "Quantity", "Price", "UserEmail", "ReceiptID", "receiptTime", "receiptItemName"]);


  // cycle through the items in firebase and map them as json-style key-value pairs.
  for (index in keys) {
          myobject = keys[index]
          receiptObject = Object.values(data).map(object => object.receipt)


          // initialise a new row to place in to the sheet
          var sheetRow = []

          // extract main values of interest from firebase.
          receiptContents = Object.values(receiptObject).map(object => object.receiptContents)
          email = Object.values(receiptObject).map(object => object.email)
          receiptID = Object.values(receiptObject).map(object => object.receiptID)
          receiptItemName = Object.values(receiptObject).map(object => object.receiptItemName)
          receiptTime = Object.values(receiptObject).map(object => object.receiptTime)

          receiptContents1 = receiptObject[index].receiptContents
          email1 = receiptObject[index].email
          receiptID1 = receiptObject[index].receiptID
          receiptItemName1 = receiptObject[index].receiptItemName
          receiptTime1 = receiptObject[index].receiptTime

          // here, we check to see if each database has already been updated on google sheets.
          // if it has not, we will extract the csv-stule information, and reconstruct it to suit our structure.
          syncCheck = receiptObject[index].synchronised


          if(syncCheck == "True") { }  // i.e. record already added to google sheets
          else {

            // deconstruct the csv and reconstruct it for our needs.
            const data1 = receiptContents1

            rows = data1.split("~");
            rows.shift()
            rows.pop()
            for (row in rows) {
              columns = rows[row].split(",")
              columns.push(email1)
              columns.push(receiptID1)
              columns.push(receiptTime1);
              columns.push(receiptItemName1);
              // after the above, add all of this to as a new row.
              sheet.appendRow(columns)

              // now that  we have done this, we need to make sure the newly added records are flagged as
              // 'synchronised' so we dont add them again.

              var object_name = Object.values(myobject)[0];
              var slash = '/';
              var receiptconcat = 'receipt';
              var syncCheckJson = slash.concat(object_name, slash, receiptconcat, slash, 'synchronised')
              base.setData(syncCheckJson, "True");

            }
          }

    }

}

