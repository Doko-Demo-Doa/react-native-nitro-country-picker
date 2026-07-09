const fs = require('fs');
const path = require('path');

/**
 * React Native's Android Gradle plugin resolves dependencies using hardcoded
 * paths relative to the example app (e.g. `../node_modules/@react-native/gradle-plugin`).
 * With pnpm's `nodeLinker: hoisted`, those packages live in the workspace root
 * `node_modules`, so we link them into `example/node_modules` so the Android
 * build can find them.
 */

const rootDir = path.resolve(__dirname, '..');
const exampleDir = path.join(rootDir, 'example');
const rootNodeModules = path.join(rootDir, 'node_modules');
const exampleNodeModules = path.join(exampleDir, 'node_modules');

const links = [
  {
    from: path.join(exampleNodeModules, 'react-native'),
    to: path.join(rootNodeModules, 'react-native'),
  },
  {
    from: path.join(exampleNodeModules, '@react-native'),
    to: path.join(rootNodeModules, '@react-native'),
  },
];

function ensureDir(dir) {
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }
}

function createLink(from, to) {
  const target = path.relative(path.dirname(from), to);

  try {
    const existing = fs.readlinkSync(from);
    if (
      existing === target ||
      path.resolve(path.dirname(from), existing) === to
    ) {
      return;
    }
    fs.unlinkSync(from);
  } catch (err) {
    if (err.code !== 'ENOENT') {
      if (fs.existsSync(from)) {
        fs.rmSync(from, { recursive: true, force: true });
      }
    }
  }

  ensureDir(path.dirname(from));

  const isWindows = process.platform === 'win32';
  fs.symlinkSync(target, from, isWindows ? 'junction' : 'dir');
}

function main() {
  if (!fs.existsSync(rootNodeModules)) {
    return;
  }

  for (const link of links) {
    if (!fs.existsSync(link.to)) {
      continue;
    }
    createLink(link.from, link.to);
  }
}

main();
