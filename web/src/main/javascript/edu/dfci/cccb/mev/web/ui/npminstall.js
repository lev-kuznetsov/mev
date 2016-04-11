var fs = require('fs')
var resolve = require('path').resolve
var join = require('path').join
var cp = require('child_process')

var lib = resolve(__dirname, '../libs/')

fs.readdirSync(lib)
      .forEach(function (mod) {
              var modPath = join(lib, mod)
          if (!fs.existsSync(join(modPath, 'package.json'))) return

          cp.spawn('npm', ['i'], { env: process.env, cwd: modPath, stdio: 'inherit' })
      })
