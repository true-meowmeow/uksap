/*
    скрипт развернут на сервисе https://script.google.com/
    обращение происходит по url из Constant
*/

function doGet(e) {

  var ss = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1IQA9hEynrvGpYLtI2AaquiDyqGArvBL0Tkr-yYAjXzY/edit#gid=0");
  var sheet = ss.getSheetByName("ae");

  return getUsers(sheet);
}


function getUsers(sheet) {
  var jo = {};
  var dataArray = [];

  var rows = sheet.getRange(1, 1, sheet.getLastRow(), sheet.getLastColumn()).getValues();

  var lastCol = sheet.getLastColumn();
  for(var i = 0, l= rows.length; i<l ; i++){
  var data = [[]];
    var dataRow = rows[i];

    for(var j = 0; j < lastCol; j++) {
      if(!data[j])
      data[j] = [];
      data[j].push(dataRow[j]);
    }
  dataArray.push(data);
  }

  jo.user = dataArray;
  var result = JSON.stringify(jo);

  return ContentService.createTextOutput(result).setMimeType(ContentService.MimeType.JSON);
}