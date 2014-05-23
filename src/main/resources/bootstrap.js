(function bootstrap (bindings) {
  var global = this;

  global.console = {
    log : function (str) {
      bindings.log(str);
    }
  };

  var bad = bindings.runScript("function bad () { throw new Error('bad thing'); }", 'file.js');

  function other () {
    bad();
  }

  try {
    other();
  } catch (e) {
    console.log(e.stack);
  }

});
