define('console', function () {

  var stdio = require.internal.api('stdio');

  function Console () {}

  Console.prototype,info =
  Console.prototype.log = function (str) {
    stdio.writeToStdout(str + '\n');
  };

  Console.prototype.error =
  Console.prototype.warn = function(str) {
    stdio.writeToStderr(str + '\n');
  };

  return Console;
});
