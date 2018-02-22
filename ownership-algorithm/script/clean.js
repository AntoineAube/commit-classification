var lineReader = require('readline').createInterface({
    input: require('fs').createReadStream(process.argv[2])
  });
  
  lineReader.on('line', function (line) {
      if(line.match("undefined") || line.match("test") || line.match("doc") || line.match("Erreur")
            || line.match("exec error:")  || line.match("example") || line.match("benchmark")){

      }else{
        console.log(line);
      }
   
  });