#!/usr/bin/env /home/corentin/.nvm/versions/node/v12.11.0/bin/node

if(process.argv[2]){
    console.log(require('crypto').createHash('sha256').update(process.argv[2]).digest('hex'));
}else {
    console.log("Error: nothing to hash");
}
