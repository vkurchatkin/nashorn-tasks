(function bootstrap (runtime) {
  var global = this;

  global.global = global;

  global.define = function (id, factory) {
    runtime.defineModule(id, factory);
  };

  global.require = function (id) {
    return runtime.requireModule(id);
  };

  global.require.internal = {};
  global.require.internal.api = function (id) {
    return runtime.getInternal(id);
  };

  var Console = runtime.requireModule('console');
  var fs = runtime.getInternal('filesystem');
  var platform = runtime.getInternal('platform');

  global.console = new Console;

  var args = Java.from(runtime.getArgs());
  var cwd = platform.getCwd();

  if (args.length === 0) {
    console.log('no script specified');
    platform.exit(-1);
  }

  var path = require('path');

  var filepath = path.join(cwd, args[0]);
  

  var src = fs.readFile(filepath);;

  runtime.runScript(src, filepath);

});
