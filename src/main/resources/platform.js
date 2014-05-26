define('platform', function () {

  var bindings = require.internal.api('platform');
  
  function hrtime () {
    return bindings.getNanotime();
  }

  function getCpus () {
    return bindings.getCpus();
  }

  /**
   * Microtasks (for cleanup only)
   */
  
  var microtaskQueue = [];
  var running = false;

  function scheduleMicrotask (fn) {
    if (running)
      throw new Error('recursive microtasks are forbidden')

    microtaskQueue.push(fn);
  }

  function runMicrotaskQueue () {
    if (running)
      throw new Error('not supposed to happen');

    running = true;

    var microtask;

    while (microtask = microtaskQueue.shift())
      microtask();

    running  = false;

  }

  global._registerMicrotaskFunction(runMicrotaskQueue);

  delete global._registerMicrotaskFunction;

  return { 
    hrtime : hrtime,
    scheduleMicrotask : scheduleMicrotask,
    getCpus : getCpus
  };

});
