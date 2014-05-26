var platform = require('platform');

platform.scheduleMicrotask(function () {
  console.log('Hello from microtask');
});

console.log('Hello from main script');
