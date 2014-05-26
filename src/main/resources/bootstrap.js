(function bootstrap (runtime) {
  var global = this;

  global.global = global;

  var knownGlobals = [
    'global',
    'console',
    'eval',
    'Infinity',
    'isFinite',
    'EvalError',
    'Function',
    'Array',
    'Error',
    'decodeURIComponent',
    'escape',
    'encodeURIComponent',
    'Uint8Array',
    'Int16Array',
    'encodeURI',
    'TypeError',
    'SyntaxError',
    'Uint8ClampedArray',
    'NaN',
    'String',
    'Number',
    'Boolean',
    'RangeError',
    'Uint16Array',
    'ArrayBuffer',
    'DataView',
    'Int8Array',
    'Float64Array',
    'parseFloat',
    'Int32Array',
    'unescape',
    'isNaN',
    'undefined',
    'ReferenceError',
    'decodeURI',
    'RegExp',
    'JSON',
    'parseInt',
    'Uint32Array',
    'Date',
    'Math',
    'URIError',
    'Object',
    'Float32Array'
  ];

  // sanitize global object

  Object.getOwnPropertyNames(global).forEach(function (prop) {
    if (knownGlobals.indexOf(prop) === -1) {
      delete global[prop];
    }
  });

  function Module (id, filename) {
    this.id = id;
    this.filename = filename;
    this.factory = null;
    this.exports = null;
    this.loading = false;
    this.loaded = false;
  }

  Module.prototype.getExports = function () {
    if (this.loaded) // exports can be falsey
      return this.exports;

    this.loading = true;
    this.exports = this.factory.call(global);
    this.loading = false;
    this.loaded = true;

    return this.exports;
  };

  function wrapAMD (src) {
    return ';(function(){' + src + '}());';
  }

  function wrapCJS (src, name) {
    var res = [];

    res.push('function wrapFn(module, exports){');
    res.push(src);
    res.push('}');

    res.push('define("' + name + '",');
    res.push('function () {');
    res.push('var module = { exports : {} };');
    res.push('wrapFn(module, module.exports)');
    res.push('return module.exports;');
    res.push('})');

    return wrapAMD(res.join('\n'));
  }

  var moduleCache = {};
  var BUILTINS = {
    console : 'amd',
    util : 'cjs',
    path : 'cjs',
    assert : 'cjs',
    tasks : 'amd',
    platform : 'amd'
  };

  var fs = runtime.getInternal('filesystem');

  function runBuiltinScript (filename, mtype, id) {
    var source = fs.readResource(filename);

    if (mtype === 'amd')
      source = wrapAMD(source);
    else if (mtype === 'cjs')
      source = wrapCJS(source, id);

    return runtime.runScript(source, filename);
  }

  global.define = function (id, factory) {
    if (!moduleCache.hasOwnProperty(id))
      throw new Error('Unknown module');

    moduleCache[id].factory = factory;
  };

  global.require = function (id) {
    if (moduleCache.hasOwnProperty(id))
      return moduleCache[id].getExports();

    var module;

    if (BUILTINS.hasOwnProperty(id)) {
      module = new Module(id, id + '.js');
      moduleCache[id] = module;
      runBuiltinScript(id + '.js', BUILTINS[id], id);
    } else {
      throw new Error('Module not found'); // TODO userland modules
    }

    if (!module.factory)
      throw new Error('Module was loaded, but not defined');

    return module.getExports();
  };

  global.require.internal = {};
  global.require.internal.api = function (id) {
    return runtime.getInternal(id);
  };

  var Console = require('console');
  var platform = runtime.getInternal('platform');

  global.console = new Console;

  var args = runtime.getArgs();
  var cwd = platform.getCwd();

  if (args.length === 0) {
    console.log('no script specified');
    platform.exit(-1);
  }

  var path = require('path');
  var filepath = args[0];

  if (!path.isAbsolute(filepath))
    filepath = path.join(cwd, args[0]);

  var src = fs.readFile(filepath);;

  try {
    runtime.runScript(src, filepath);
  } catch (e) {
    console.error(e.getCause().message);
    console.error(runtime.getErrorStack(e.getCause()));
    platform.exit(-1);
  }

});
