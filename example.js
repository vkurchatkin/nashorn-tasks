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
  var r = pi(10000000) + e(10000000);
  console.log(r);
});
