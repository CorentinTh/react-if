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

console.log(os.cpus());

function cpuAverage() {

    let totalIdle = 0, totalTick = 0;
    const cpus = os.cpus();

    for (const {times} of cpus){
        for (const type in times){
            totalTick += times[type];
        }

        totalIdle += times.idle;
    }

    return {idle: totalIdle / cpus.length,  total: totalTick / cpus.length};
}

var startMeasure = cpuAverage();

//Set delay for second Measure
setTimeout(function() {

    //Grab second Measure
    var endMeasure = cpuAverage();

    //Calculate the difference in idle and total time between the measures
    var idleDifference = endMeasure.idle - startMeasure.idle;
    var totalDifference = endMeasure.total - startMeasure.total;

    //Calculate the average percentage CPU usage
    var percentageCPU = 100 - Math.floor(100 * idleDifference / totalDifference);

    //Output result to console
    console.log(percentageCPU + "% CPU Usage.");

}, 100);