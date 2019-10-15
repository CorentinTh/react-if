#!/usr/bin/env /home/corentin/.nvm/versions/node/v12.11.0/bin/node

const os = require('os');

console.log(JSON.stringify({
    timestamp: Date.now(),
    hostname: os.hostname(),
    platform: os.platform(),
    uptime: os.uptime(),
    freemem: os.freemem(),
    loadavg: os.loadavg()
}, null, 4));
