define('tasks', function () {
  var platform = require.internal.api('platform');

  /**
   * Task queue
   */

  function TaskQueue (options) {
    if (!(this instanceof TaskQueue))
      return new TaskQueue(options);

    options = options || {};

    this._concurrency = options.concurrency || platform.getCpus();
  }

  TaskQueue.prototype.createTask = function (fn) {
    return new Task(this, fn);
  };

  /**
   * Task
   */

  function Task (queue, fn) {
    if (!queue || ! (queue instanceof TaskQueue))
      throw new TypeError('queue is required');

    if ('function' !== typeof fn)
      throw new TypeError('`fn` should be a function');

    this._queue = queue;
    this._fn = fn;

    this.finished = false;
    this.running = false;

    this._dependencies = [];
  }

  Task.prototype.run = function () {

  };

  Task.prototype.isReady = function () {
    for (var i = 0; i < this._dependencies.length; i++)
      if (!this._dependencies[i].finished) return false;

    return true;
  };

  Task.prototype.addDependency = function (dependency) {
    if (!(dependency instanceof Task))
      throw new TypeError('`dependecy` should be a Task');

    if (!dependecy._queue)
      dependency._queue = this._queue;

    if (dependency._queue !== this._queue)
      throw new Error('Can\'t have dependency from other queue');

    this._dependencies.push(dependency);

    return this;
  };

   /**
    * Exports
    */

  var defaultQueue = new TaskQueue();


  defaultQueue.createQueue = function (options) {
    return new TaskQueue(options);
  };

  defaultQueue.Queue = TaskQueue;
  defaultQueue.Task = Task;

  return defaultQueue;
});
