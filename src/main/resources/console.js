define('console', function () {

  var stdio = require.internal.api('stdio');
  var util = require('util');

  function Console () {}

  Console.prototype.info =
  Console.prototype.log = function () {
    stdio.writeToStdout(util.format.apply(this, arguments) + '\n');
  };

  Console.prototype.error =
  Console.prototype.warn = function () {
    stdio.writeToStderr(util.format.apply(this, arguments) + '\n');
  };

  return Console;
});
