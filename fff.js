console.log({
  'some_field' : 22343,
  arr : [1, 2, 3]
});

try {
  throw new Error('test')
} catch (e) {
  console.log(e.stack);
}