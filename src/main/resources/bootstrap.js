(function bootstrap (bindings) {
  var global = this;

  global.define = function (id, factory) {
    bindings.defineModule(id, factory);
  };

  global.require = function (id) {
    return bindings.require(id);
  };

  global.require.internal = {};
  global.require.internal.api = function (id) {
    return bindings.getInternal(id);
  };

  var Console = bindings.requireModule('console');

  global.console = new Console;

  var args = Java.from(bindings.getArgs());
  var cwd = bindings.getCwd();

  if (args.length === 0) {
    console.log('no script specified');
    bindings.exit(-1);
  }

  var filepath = cwd + '/' + args[0]; // TODO normalize, etc.
  

  var src = bindings.readFile(filepath);;

  bindings.runScript(src, filepath);

});
