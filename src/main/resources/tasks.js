define('tasks', function () {
  var platform = require.internal.api('platform');

  function TaskQueue (options) {
    if (!(this instanceof TaskQueue))
      return new TaskQueue(options);

    options = options || {};

    this._concurrency = options.concurrency || platform.getCpus();
  }


  var defaultQueue = new TaskQueue();

  defaultQueue.createQueue =
  defaultQueue.Queue = TaskQueue;

  return defaultQueue;
});
