// This exports the internal configuration of eslint
// for eslint cli to work, specially with IDEs
module.exports = require('neutrino/src/api')().call('eslintrc')
