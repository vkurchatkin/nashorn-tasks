# concurrent tasks for Nashorn
inspired by `NSOperationQueue`

# Prerequisites

 - Java 8;
 - maven 3.x.x;
 - node.js 0.10.x (optional);
 - npm (optional);

# Build

 - `mvn package` or
 - `npm run build`.

# Run example

 - `npm run example` or
 - `./runtime example.js`

# Run tests

 - `npm test` or
 - `./runtime test/test.js`

# API

### Require:

```javascript
var tasks = require('tasks');
```

### Create queue:

```javascript
var queue = tasks.createQueue({ concurrency : 2 });
```

or use `tasks` as default queue.

### Create a task:

```javascript
var task = queue.createTask(function (numbers) {
  return numbers.reduce(function (p, q) {
    return p + q;
  }, 0);
}, [1, 2, 3]);

```

Caveats: 

 - task can't reference anything from outer scope;
 - input and output are serialized via `JSON.stringify`;
 - `require` doesn't work;

### Set up dependencies:

```javascript
var first = queue.createTask(function (numbers) {
  return numbers.map(function (n) {
    return n + 1;
  });
}, [1, 2, 3]);

var second = queue.createTask(function (numbers) {
  return numbers.reduce(function (p, q) {
    return p + q;
  }, 0);
});

second.addDependency(first);
```

`first` gets input (second argument of `createTasks`) as an argument.
`second` has no `input` specified, so it gets output of it's dependencies.

### Run queue:

```javascript
queue.run();
```

This method blocks until queue is empty.

### Check results:

```javascript
console.log(first.result);
console.log(second.result);
```

# License

MIT