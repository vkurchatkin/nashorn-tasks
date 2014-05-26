define('tasks', function () {
  var platform = require.internal.api('platform');
  var bindings = require.internal.api('tasks');

  /**
   * Topological sort
   */

  var kTempMark = 1;
  var kMark = 2;
  var kKey = '__mark__';

  function tsort (nodes, depsFn) {
    var res = [];

    for (var i = 0; i < nodes.length; i++)
      if (!nodes[i][kKey])
        tvisit(nodes[i], depsFn, res);

    res.forEach(function (node) {
      delete node[kKey];
    });

    return res;
  }

  function tvisit (node, depsFn, res) {
    if (node[kKey] === kMark) return;

    if (node[kKey] === kTempMark)
      throw new Error('Cycle detected');

    node[kKey] = kTempMark;

    depsFn(node).forEach(function (n) {
      tvisit(n, depsFn, res);
    });

    node[kKey] = kMark;

    res.push(node);
  }

  /**
   * Task queue
   */

  function TaskQueue (options) {
    if (!(this instanceof TaskQueue))
      return new TaskQueue(options);

    options = options || {};

    this._concurrency = options.concurrency || platform.getCpus();
    this._tasks = [];
    this._running = false;
    this._executor = bindings.createExecutor(this._concurrency);
  }

  TaskQueue.prototype.addTask = function (task) {
    if (this._running)
      throw new Error('Can\'t add task while running (yet)');

    this._tasks.push(task);
  };

  TaskQueue.prototype.createTask = function (fn) {
    return new Task(this, fn);
  };

  TaskQueue.prototype.run = function () {
    if (this._running)
      throw new Error('already running');

    if (this._tasks.length === 0)
      return;

    this._running = true;

    var tasks = tsort(this._tasks, function (task) { return task._dependencies; });
    this._tasks = [];

    var pending = tasks.length;

    var concurrency = this._concurrency;

    tasks.splice(0, concurrency).forEach(function (task) {
      task.run();
    });

    var shouldRun = true;

    while (pending) {
      var job = this._executor.nextJob();
      var task = job.getData();

      if (!task)
        throw new Error('not supposed to ever happen');

      task._running = false;
      task.finished = true;
      task.result = JSON.parse(job.getResult());
      pending--;
    }

    this._running = false;
  };

  /**
   * Task
   */

  function Task (queue, fn, input) {
    if (!queue || ! (queue instanceof TaskQueue))
      throw new TypeError('queue is required');

    if ('function' !== typeof fn)
      throw new TypeError('`fn` should be a function');

    queue.addTask(this);
    this._queue = queue;
    this._input = input;

    this.finished = false;
    this.running = false;

    this._dependencies = [];
    this._fn = fn;
    this._job = null;
    this.result = undefined;
  }

  /**
   * Create task source with embedded input and dependency results
   */

  function createSource (fn, input, dependencies) {
  var src = [];

  dependencies = dependencies || [];

  var args = input == undefined ? dependencies : [input].concat(dependencies);

  return ';JSON.stringify((' + fn.toString() + ').apply(this, JSON.parse(\'' + JSON.stringify(args) + '\')));';
}


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
   * Run task (private for now)
   */
  
  Task.prototype.run = function () {
    if (this.running)
      throw new Error('task is already running');

    if (!this.isReady())
      throw new Error('task is not ready to run');

    var src = createSource(this._fn, this._input, this._dependencies.map(function (t) { return t.result; }));

    this._job = bindings.createJob(src, this);

    this._queue._executor.submit(this._job);
    this._running = true;
  }

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
