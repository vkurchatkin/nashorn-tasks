console.log('Yay!!!');

try {
  throw new Error('test')
} catch (e) {
  console.log(e.stack);
}