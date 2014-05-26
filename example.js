var tasks = require('tasks');
var platform = require('platform');

function pi (iterations) {
  var r = 1;
  var s = -1;
  var d = 3;

  for (var i = 0; i < iterations; i++) {
    r += s / d;
    d += 2;
    s *= -1;
  }

  return r * 4;
}

function e (iterations) {
  var r = 1;
  var d = 1;
  var c = 1;

  for (var i = 0; i < iterations; i++) {
    r += 1 / d;
    d = d * (++c);
  }

  return r;
}

function bench (name, fn) {
  console.log(name);
  var before = platform.hrtime();
  fn();
  var after = platform.hrtime();
  var total = after - before;

  console.log(total + ' ns');
}

bench('pi + e sequential', function () {
  var r = pi(100000000) + e(100000000) + pi(100000000) + e(100000000);
  console.log(r);
});


var piTask1 = tasks.createTask(pi, 100000000);
var piTask2 = tasks.createTask(pi, 100000000);

var eTask1 = tasks.createTask(e, 100000000);
var eTask2 = tasks.createTask(e, 100000000);


var sumTask = tasks.createTask(function (pi1, pi2, e1, e2) {
  return pi1 + pi2 + e1 + e2;
});

sumTask
  .addDependency(piTask1)
  .addDependency(piTask2)
  .addDependency(eTask1)
  .addDependency(eTask2);

bench('pi + e parallel', function () {
  tasks.run();
  console.log(sumTask.result);
});
