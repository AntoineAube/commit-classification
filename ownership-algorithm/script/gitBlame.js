const fs = require('fs');
var gitblame = require('gitblame');
var exec = require('child_process').exec;
// fonction qui recupère le nom du contributeurs
function getNameContributors(str){
  var authors = str.split("(");
  if(authors.length > 1){
    return authors[1].split(" 2")[0];
  }else{
    //console.log("Erreur de split");
  }
 
}

function isEquals(str,str1){
  var res = str.toUpperCase();
  var res1 = str1.toUpperCase();
  
  if(res == res1){
    return true;
  }else{
    return false;
  }
}

function percen(n,n1){
  return (n1 * 100) / n;
}

// cherche si un element est dans un tableau 

function searchInArray(array,element){
  for(var i = 0; i < array.length; i++){
    if(isEquals(array[i],element)){
      return true;
    }
  }
  return false;
}

// git blame

function blame(filename){
 
  gitblame(filename, function(err, lines){

      if(lines){
        if(lines.length > 1){
          var isApprentissage = false;
          var regexAlgoType = "def fit";
          var linesAut = [];
          var contribPercentbyAuts = [];
          var linesOfOthorsOne = 1;
          var linesOfOthorstwo = 1;
          var arrayContributors = [];
          var arrayAuthersContrib = [];
          var temp = lines.length - 1;
          var start = 1;
          for(var i = start; i < temp; i++){
            if(!searchInArray(arrayContributors,getNameContributors(lines[i]))){
                arrayContributors.push(getNameContributors(lines[i]));
            }
          }
          for(var i = 0; i < arrayContributors.length; i++){
            linesAut[i] = 1;
          }
          var tabLines = "{ linesLines :" + lines.length + "}";
          for(var i = 2; i < lines.length - 1 ;i++){
            if(lines[i].match(regexAlgoType)){
              isApprentissage = true;
            }
            for(var j = 0; j < arrayContributors.length; j++){
              if(isEquals(getNameContributors(lines[i]),arrayContributors[j])){
                linesAut[j]++;
              }
            }
          }
        }else{
          console.log("le nombre de ligne est inferieur à 1");
        }
       
        var nbLines =  lines.length - 2;
        for(var i = 0 ; i < arrayContributors.length; i++){
          contribPercentbyAuts[i] =  percen(nbLines,linesAut[i]);
        }
        var result = filename;
        var resultEntete = "File";
        for(var i = 0; i < arrayContributors.length; i++){
          resultEntete+=";" + "Author"+(i+1) + ";" + "Line"+(i+1) + ";" + "Contribution"+(i+1);
          result+=";" + arrayContributors[i] +";" + linesAut[i] + ";" +  contribPercentbyAuts[i];
          //console.log(arrayContributors[i] +";" + linesAut[i] + ";" +  contribPercentbyAuts[i]);
        }
        resultEntete+=";" + "TotalLines" + ";" + "Learning Algrithme"
        result+=";" + nbLines + ";" + isApprentissage + ";" + arrayContributors.length;
        //console.log(result);
        if(arrayContributors.length == 10){
          //console.log(resultEntete);
          console.log(result);
        }
    
    }else{
     
    }
   
  });
}

var execute = function(command){
  exec(command, {maxBuffer: 2048 * 5000}, function(error, stdout, stderr){});
};

execute(blame(process.argv[2]));

