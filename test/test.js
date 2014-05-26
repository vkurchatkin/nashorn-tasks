var assert = require('assert');
var tasks = require('tasks');

function noop () {}

var Task = tasks.Task;
var TaskQueue = tasks.Queue;

// tasks is instance of TaskQueue
assert(tasks instanceof TaskQueue);

// TasksQueue#createTask should create instance of Task
assert(tasks.createTask(noop) instanceof Task);
// it's queue should be `tasks`
assert.equal(tasks.createTask(noop)._queue, tasks);

// createQueue should create a new Task Queue
assert(tasks.createQueue() instanceof TaskQueue);

