(function bootstrap (bindings) {
  var global = this;

  var console = {
    log : function (str) {
      bindings.log(str);
    }
  };

  function test () {
    outer();
  }

  function bad () {
    test();
  }

  try {
    bad();
  } catch (e) {
    console.log(e.stack)
  }

});

function outer () {
  throw new Error('test')
}
