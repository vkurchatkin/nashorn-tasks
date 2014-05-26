define('platform', function () {

  var bindings = require.internal.api('platform');
  
  function hrtime () {
    return bindings.getNanotime();
  }

  return { hrtime : hrtime };
});
