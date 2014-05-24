(function bootstrap (bindings) {
  var global = this;

  global.global = global;

  global.define = function (id, factory) {
    bindings.defineModule(id, factory);
  };

  global.require = function (id) {
    return bindings.requireModule(id);
  };

  global.require.internal = {};
  global.require.internal.api = function (id) {
    return bindings.getInternal(id);
  };

  var Console = bindings.requireModule('console');
  var fs = bindings.getInternal('filesystem');
  var platform = bindings.getInternal('platform');

  global.console = new Console;

  var args = Java.from(bindings.getArgs());
  var cwd = platform.getCwd();

  if (args.length === 0) {
    console.log('no script specified');
    platform.exit(-1);
  }

  var path = require('path');

  var filepath = path.join(cwd, args[0]);
  

  var src = fs.readFile(filepath);;

  bindings.runScript(src, filepath);

});
