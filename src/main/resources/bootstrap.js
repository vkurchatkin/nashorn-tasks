(function bootstrap (bindings) {
  var global = this;

  global.console = {
    log : function (str) {
      bindings.log(str);
    }
  };

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
